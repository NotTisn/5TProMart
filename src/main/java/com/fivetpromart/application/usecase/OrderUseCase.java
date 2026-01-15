package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.CheckProductResultDto;
import com.fivetpromart.application.dto.OrderDto;
import com.fivetpromart.application.dto.command.CheckProductCommand;
import com.fivetpromart.application.dto.command.OrderCreationCommand;
import com.fivetpromart.application.mapper.OrderDataMapper;
import com.fivetpromart.application.port.in.IOrderUseCasePort;
import com.fivetpromart.application.port.out.ICustomerRepository;
import com.fivetpromart.application.port.out.IOrderRepository;
import com.fivetpromart.application.port.out.IProductRepository;
import com.fivetpromart.application.port.out.IStockInventoryRepository;
import com.fivetpromart.domain.exception.ExpiredLotException;
import com.fivetpromart.domain.exception.InsufficientStockException;
import com.fivetpromart.domain.exception.InvalidOrderException;
import com.fivetpromart.domain.exception.LotNotFoundException;
import com.fivetpromart.domain.exception.OrderNotFoundException;
import com.fivetpromart.domain.model.Customer;
import com.fivetpromart.domain.model.Order;
import com.fivetpromart.domain.model.Product;
import com.fivetpromart.domain.model.StockInventory;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderUseCase implements IOrderUseCasePort {

    private final IOrderRepository orderRepository;
    private final IStockInventoryRepository stockInventoryRepository;
    private final IProductRepository productRepository;
    private final ICustomerRepository customerRepository;
    private final OrderDataMapper mapper;

//    @Override
//    @Transactional(readOnly = true)
//    public Page<OrderDto> searchOrders(OrderSearchQuery query, Pageable pageable) {
//        log.info("Searching orders with query: {}", query);
//        // Search orders using repository
//        Page<Order> orderPage = orderRepository.searchOrders(query, pageable);
//
//        // Map to DTO
//        return orderPage.map(mapper::toDto);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public OrderDto getOrderById(String orderId) {
//        log.info("Getting order by ID: {}", orderId);
//
//        // Find order
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new OrderNotFoundException(orderId));
//
//        // Map to DTO
//        return mapper.toDto(order);
//    }

    @Override
    @Transactional(readOnly = true)
    public CheckProductResultDto checkProduct(CheckProductCommand command) {
        log.info("Checking product for lotId: {}, quantity: {}", command.getLotId(), command.getQuantity());

        // 1. Find lot (StockInventory)
        StockInventory lot = stockInventoryRepository.findById(command.getLotId())
                .orElseThrow(() -> new LotNotFoundException(command.getLotId()));

        // 2. Check if lot is expired
        if (lot.getExpirationDate().isBefore(LocalDate.now())) {
            throw new ExpiredLotException(command.getLotId());
        }

        // 3. Check if sufficient stock
        Long requestedQuantity = command.getQuantity() != null ? command.getQuantity() : 1L;
        if (lot.getStockQuantity() < requestedQuantity) {
            throw new InsufficientStockException(lot.getStockQuantity(), requestedQuantity);
        }

        // 4. Get product information
        Product product = productRepository.findById(lot.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + lot.getProductId()));

        // 5. Calculate subtotal
        BigDecimal unitPrice = product.getSellingPrice();
        BigDecimal subTotal = unitPrice.multiply(BigDecimal.valueOf(requestedQuantity));

        // 6. Build result DTO
        return CheckProductResultDto.builder()
                .lotId(lot.getLotId())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .unitOfMeasure(product.getUnitOfMeasure())
                .unitPrice(unitPrice)
                .quantity(requestedQuantity)
                .subTotal(subTotal)
                .currentStock(lot.getStockQuantity())
                .status(lot.getStatus())
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
                
                // Check expiration
                if (lot.getExpirationDate().isBefore(LocalDate.now())) {
                    throw new ExpiredLotException(itemCmd.getLotId());
                }
                
                // Check stock
                if (lot.getStockQuantity() < itemCmd.getQuantity()) {
                    throw new InsufficientStockException(lot.getStockQuantity(), itemCmd.getQuantity());
                }
                
                // Get product
                Product product = productRepository.findById(lot.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + lot.getProductId()));
                
                // Create order item
                Order.OrderItem orderItem = Order.OrderItem.create(
                        lot.getLotId(),
                        product.getProductId(),
                        product.getProductName(),
                        itemCmd.getQuantity(),
                        product.getSellingPrice()
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
            for (OrderCreationCommand.OrderItemCommand itemCmd : command.getItems()) {
                StockInventory lot = stockInventoryRepository.findById(itemCmd.getLotId())
                        .orElseThrow(() -> new LotNotFoundException(itemCmd.getLotId()));
                
                // Deduct quantity
                long newQuantity = lot.getStockQuantity() - itemCmd.getQuantity();
                lot.update(
                        lot.getProductId(),
                        lot.getManufactureDate(),
                        lot.getExpirationDate(),
                        newQuantity,
                        lot.getImportPrice(),
                        lot.getStatus()
                );
                
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
}
