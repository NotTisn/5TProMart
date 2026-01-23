package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.CancelOrderResultDto;
import com.fivetpromart.application.dto.CheckProductResultDto;
import com.fivetpromart.application.dto.OrderDto;
import com.fivetpromart.application.dto.command.CancelOrderCommand;
import com.fivetpromart.application.dto.command.CheckProductCommand;
import com.fivetpromart.application.dto.command.OrderCreationCommand;
import com.fivetpromart.application.dto.query.OrderSearchQuery;
import com.fivetpromart.application.mapper.OrderDataMapper;
import com.fivetpromart.application.port.in.IOrderUseCasePort;
import com.fivetpromart.application.port.out.ICustomerRepository;
import com.fivetpromart.application.port.out.IOrderRepository;
import com.fivetpromart.application.port.out.IProductRepository;
import com.fivetpromart.application.port.out.IPromotionRepository;
import com.fivetpromart.application.port.out.IStockInventoryRepository;
import com.fivetpromart.application.port.out.IStockReservationRepository;
import com.fivetpromart.domain.exception.*;
import com.fivetpromart.domain.model.Customer;
import com.fivetpromart.domain.model.Order;
import com.fivetpromart.domain.model.Product;
import com.fivetpromart.domain.model.Promotion;
import com.fivetpromart.domain.model.StockInventory;
import com.fivetpromart.domain.model.StockReservation;
import com.fivetpromart.domain.model.strategy.discount.*;
import com.fivetpromart.domain.model.strategy.notification.*;
//import com.fivetpromart.domain.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderUseCase implements IOrderUseCasePort {

    private final IOrderRepository orderRepository;
    private final IStockInventoryRepository stockInventoryRepository;
    private final IStockReservationRepository reservationRepository;
    private final IProductRepository productRepository;
    private final ICustomerRepository customerRepository;
    private final IPromotionRepository promotionRepository;
    private final OrderDataMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> searchOrders(OrderSearchQuery query, Pageable pageable) {
        log.info("Searching orders with query: {}", query);
        // Search orders using repository
        Page<Order> orderPage = orderRepository.searchOrders(query, pageable);

        // Map to DTO
        return orderPage.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(String orderId) {
        log.info("Getting order by ID: {}", orderId);

        // Find order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // Map to DTO
        return mapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public CheckProductResultDto checkProduct(CheckProductCommand command) {
        log.info("Checking product for lotId: {}, quantity: {}", command.getLotId(), command.getQuantity());

        // 1. Find lot (StockInventory)
        StockInventory lot = stockInventoryRepository.findById(command.getLotId())
                .orElseThrow(() -> new LotNotFoundException(command.getLotId()));

        // 2. Check if lot is expired (null expirationDate means non-perishable, never expires)
        if (lot.getExpirationDate() != null && lot.getExpirationDate().isBefore(LocalDate.now())) {
            throw new ExpiredLotException(command.getLotId());
        }

        // 3. Check if sufficient AVAILABLE stock (total - reserved)
        Long requestedQuantity = command.getQuantity() != null ? command.getQuantity() : 1L;
        Long availableStock = lot.getAvailableQuantity();
        if (availableStock < requestedQuantity) {
            throw new InsufficientStockException(availableStock, requestedQuantity);
        }

        // 4. Get product information
        Product product = productRepository.findById(lot.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + lot.getProductId()));

        // 5. Calculate subtotal
        BigDecimal unitPrice = product.getSellingPrice();
        BigDecimal subTotal = unitPrice.multiply(BigDecimal.valueOf(requestedQuantity));
        
        // 6. Check for active promotions on this product
        CheckProductResultDto.PromotionInfo promotionInfo = null;
        List<Promotion> activePromotions = promotionRepository.findActivePromotionsByProductId(product.getProductId());
        
        if (!activePromotions.isEmpty()) {
            // Use the first active promotion (could be enhanced to pick "best" promotion)
            Promotion promo = activePromotions.get(0);
            
            BigDecimal promotionalPrice = unitPrice;
            BigDecimal savings = BigDecimal.ZERO;
            
            // Calculate promotional price based on promotion type
            if ("Discount".equalsIgnoreCase(promo.getPromotionType()) && promo.getDiscountPercent() != null) {
                // Percentage discount: e.g., 20% off
                BigDecimal discountRate = BigDecimal.valueOf(promo.getDiscountPercent()).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                savings = unitPrice.multiply(discountRate).setScale(0, RoundingMode.HALF_UP);
                promotionalPrice = unitPrice.subtract(savings);
            }
            // For "Buy X Get Y", we don't change unit price - frontend handles quantity-based logic
            
            promotionInfo = CheckProductResultDto.PromotionInfo.builder()
                    .promotionId(promo.getPromotionId())
                    .promotionName(promo.getPromotionName())
                    .promotionType(promo.getPromotionType())
                    .discountPercent(promo.getDiscountPercent())
                    .buyQuantity(promo.getBuyQuantity())
                    .getQuantity(promo.getGetQuantity())
                    .promotionalPrice(promotionalPrice)
                    .savings(savings)
                    .build();
            
            log.info("Found active promotion '{}' for product '{}'", promo.getPromotionName(), product.getProductName());
        }

        // 7. Build result DTO
        return CheckProductResultDto.builder()
                .lotId(lot.getLotId())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .unitOfMeasure(product.getUnitOfMeasure())
                .unitPrice(unitPrice)
                .quantity(requestedQuantity)
                .subTotal(subTotal)
                .currentStock(lot.getAvailableQuantity()) // Available = total - reserved
                .status(lot.getStatusValue())
                .promotion(promotionInfo)
                .build();
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderCreationCommand command) {
        log.info("Creating order for staffId: {}, customerId: {}", 
                command.getStaffId(), command.getCustomerId());

        try {
            // 1. Validate and prepare order items
            List<Order.OrderItem> orderItems = new ArrayList<>();
            
            for (OrderCreationCommand.OrderItemCommand itemCmd : command.getItems()) {
                // Get lot and product information
                StockInventory lot = stockInventoryRepository.findById(itemCmd.getLotId())
                        .orElseThrow(() -> new LotNotFoundException(itemCmd.getLotId()));
                
                // Check expiration (null expirationDate means non-perishable, never expires)
                if (lot.getExpirationDate() != null && lot.getExpirationDate().isBefore(LocalDate.now())) {
                    throw new ExpiredLotException(itemCmd.getLotId());
                }
                
                // Check stock availability
                // Note: Uses stockQuantity (not available) because if reserved,
                // that stock is already earmarked for this transaction
                if (lot.getStockQuantity() < itemCmd.getQuantity()) {
                    throw new InsufficientStockException(lot.getStockQuantity(), itemCmd.getQuantity());
                }
                
                // Get product
                Product product = productRepository.findById(lot.getProductId())
                        .orElseThrow(() -> new ProductNotFoundException("Product not found: " + lot.getProductId()));
                
                // Determine unit price: use FE-provided price if available, else product's sellingPrice
                BigDecimal unitPrice = itemCmd.getUnitPrice() != null 
                        ? itemCmd.getUnitPrice() 
                        : product.getSellingPrice();
                
                // Determine original unit price for tracking
                BigDecimal originalUnitPrice = itemCmd.getOriginalUnitPrice() != null 
                        ? itemCmd.getOriginalUnitPrice() 
                        : product.getSellingPrice();
                
                // Create order item with promotion tracking
                Order.OrderItem orderItem = Order.OrderItem.create(
                        lot.getLotId(),
                        product.getProductId(),
                        product.getProductName(),
                        itemCmd.getQuantity(),
                        unitPrice,
                        originalUnitPrice,
                        itemCmd.getPromotionId(),
                        itemCmd.getIsFreeItem()
                );
                
                orderItems.add(orderItem);
            }
            
            // 2. Create discount strategy (Polymorphism!)
            DiscountStrategy discountStrategy = createDiscountStrategy(command.getDiscount());
            
            // 3. Create notification strategy (Polymorphism!)
            NotificationStrategy notificationStrategy = createNotificationStrategy();
            
            // 4. Create order domain model (with polymorphic strategies)
            Order order = Order.create(
                    command.getStaffId(),
                    command.getCustomerId(),
                    command.getPaymentMethod(),
                    command.getAmountGiven(),
                    orderItems,
                    discountStrategy,        // Polymorphic discount
                    notificationStrategy     // Polymorphic notification
            );
            log.info("Domain Order staffId: {}", order.getStaffId());
            // Link items to order
            order.getItems().forEach(item -> item.setOrderId(order.getOrderId()));
            
            // 5. Update stock quantities (deduct from inventory)
            // PROPER FLOW: Check for active reservations and commit them
            for (OrderCreationCommand.OrderItemCommand itemCmd : command.getItems()) {
                StockInventory lot = stockInventoryRepository.findById(itemCmd.getLotId())
                        .orElseThrow(() -> new LotNotFoundException(itemCmd.getLotId()));
                
                // Check if there are active reservations for this lot
                List<StockReservation> activeReservations = 
                        reservationRepository.findActiveByLotId(itemCmd.getLotId());
                
                long remainingToDeduct = itemCmd.getQuantity();
                
                // First, commit any matching reservations
                for (StockReservation reservation : activeReservations) {
                    if (remainingToDeduct <= 0) break;
                    
                    long toCommit = Math.min(reservation.getQuantity(), remainingToDeduct);
                    
                    // Commit reservation (reduces both reserved and stock)
                    lot.commitReservedStock(toCommit);
                    
                    // Mark reservation as committed
                    reservation.commit(order.getOrderId());
                    reservationRepository.save(reservation);
                    
                    remainingToDeduct -= toCommit;
                    log.info("Committed {} units from reservation {} for order {}", 
                            toCommit, reservation.getReservationId(), order.getOrderId());
                }
                
                // If any quantity remains (not covered by reservations), deduct directly
                if (remainingToDeduct > 0) {
                    // Use deductForSale to properly reduce shelf quantity (customers buy from shelf)
                    lot.deductForSale(remainingToDeduct);
                    log.info("Direct deduction of {} units from lot {} (no reservation)", 
                            remainingToDeduct, itemCmd.getLotId());
                }
                
                stockInventoryRepository.save(lot);
            }
            
            // 6. Update customer loyalty points if customer exists
            if (command.getCustomerId() != null && !command.getCustomerId().isBlank()) {
                Customer customer = customerRepository.findById(command.getCustomerId())
                        .orElse(null);
                
                if (customer != null) {
                    // Deduct loyalty points if used for discount
                    if (discountStrategy instanceof LoyaltyPointsDiscountStrategy loyaltyDiscount) {
                        Long pointsUsed = loyaltyDiscount.getPointsToUse();
                        customer.redeemPoints(pointsUsed);
                        order.setPointsUsed(pointsUsed);  // Track for cancellation reversal
                        log.info("Customer {} redeemed {} loyalty points", 
                                command.getCustomerId(), pointsUsed);
                    }
                    
                    // Earn new loyalty points from purchase
                    customer.earnPoints(order.getPointsEarned());
                    customerRepository.save(customer);
                    
                    log.info("Customer {} earned {} loyalty points", 
                            command.getCustomerId(), order.getPointsEarned());
                }
            }
            
            // 7. Save order
            Order savedOrder = orderRepository.save(order);
            
            log.info("Order created successfully: {}", savedOrder.getOrderId());
            
            // 8. Map to DTO and return
            return mapper.toDto(savedOrder);
            
        } catch (LotNotFoundException | ExpiredLotException | InsufficientStockException e) {
            log.error("Order creation failed due to validation error", e);
            throw e;
        } catch (Exception e) {
            log.error("Order creation failed", e);
            throw new RuntimeException("Failed to create order", e);
        }
    }
    
    // =================================================================
    // HELPER METHODS FOR POLYMORPHISM
    // =================================================================
    
    /**
     * Create discount strategy based on command (Polymorphism!)
     */
    private DiscountStrategy createDiscountStrategy(OrderCreationCommand.DiscountCommand discountCmd) {
        if (discountCmd == null) {
            return new NoDiscountStrategy();
        }
        
        String type = discountCmd.getType();
        if (type == null || type.isBlank()) {
            return new NoDiscountStrategy();
        }
        
        return switch (type.toUpperCase()) {
            case "PERCENTAGE" -> new PercentageDiscountStrategy(
                    discountCmd.getPercentage(),
                    discountCmd.getMaxAmount()
            );
            case "FIXED_AMOUNT" -> new FixedAmountDiscountStrategy(
                    discountCmd.getAmount()
            );
            case "LOYALTY_POINTS" -> new LoyaltyPointsDiscountStrategy(
                    discountCmd.getPointsToUse()
            );
            case "NONE" -> new NoDiscountStrategy();
            default -> throw new InvalidOrderException(
                    "Unsupported discount type: " + type + 
                    ". Supported types: PERCENTAGE, FIXED_AMOUNT, LOYALTY_POINTS, NONE"
            );
        };
    }
    
    /**
     * Create notification strategy (Polymorphism!)
     */
    private NotificationStrategy createNotificationStrategy() {
        // Use composite strategy to send notifications through multiple channels
        CompositeNotificationStrategy composite = new CompositeNotificationStrategy();
        composite.addStrategy(new EmailNotificationStrategy());
        composite.addStrategy(new SmsNotificationStrategy());
        return composite;
    }

    @Override
    @Transactional
    public CancelOrderResultDto cancelOrder(CancelOrderCommand command) {
        log.info("Cancelling order: {} by staff: {}", command.getOrderId(), command.getStaffId());

        // 1. Find order
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(command.getOrderId()));

        // 2. Cancel order (uses State Pattern in domain)
        order.cancel();

        // 3. Restore stock quantities
        boolean stockRestored = false;
        try {
            for (Order.OrderItem item : order.getItems()) {
                StockInventory lot = stockInventoryRepository.findById(item.getLotId())
                        .orElse(null);
                
                if (lot != null) {
                    // Use restoreForCancellation to properly restore shelf quantity
                    lot.restoreForCancellation(item.getQuantity());
                    stockInventoryRepository.save(lot);
                }
            }
            stockRestored = true;
        } catch (Exception e) {
            log.warn("Failed to restore stock for cancelled order {}: {}", 
                    command.getOrderId(), e.getMessage());
        }

        // 4. Restore loyalty points if customer exists
        if (order.getCustomerId() != null && !order.getCustomerId().isBlank()) {
            try {
                Customer customer = customerRepository.findById(order.getCustomerId())
                        .orElse(null);
                if (customer != null) {
                    // Return used points
                    if (order.getPointsUsed() != null && order.getPointsUsed() > 0) {
                        customer.earnPoints(order.getPointsUsed());
                        log.info("Restored {} used loyalty points to customer {}", 
                                order.getPointsUsed(), order.getCustomerId());
                    }
                    // Take back earned points
                    if (order.getPointsEarned() != null && order.getPointsEarned() > 0) {
                        long toDeduct = Math.min(order.getPointsEarned(), customer.getLoyaltyPoints());
                        if (toDeduct > 0) {
                            customer.redeemPoints(toDeduct);
                            log.info("Deducted {} earned loyalty points from customer {}", 
                                    toDeduct, order.getCustomerId());
                        }
                    }
                    customerRepository.save(customer);
                }
            } catch (Exception e) {
                log.warn("Failed to restore loyalty points for cancelled order {}: {}", 
                        command.getOrderId(), e.getMessage());
            }
        }

        // 5. Save cancelled order
        Order savedOrder = orderRepository.save(order);

        log.info("Order {} cancelled successfully, stock restored: {}", 
                command.getOrderId(), stockRestored);

        // 6. Return result
        return CancelOrderResultDto.builder()
                .orderId(savedOrder.getOrderId())
                .status(savedOrder.getStatus())
                .cancelledAt(LocalDateTime.now())
                .cancelledBy(command.getStaffId())
                .reason(command.getReason())
                .stockRestored(stockRestored)
                .build();
    }
}
