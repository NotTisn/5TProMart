package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.CheckProductResultDto;
import com.fivetpromart.application.dto.OrderDto;
import com.fivetpromart.application.dto.command.CheckProductCommand;
import com.fivetpromart.application.dto.command.OrderCreationCommand;
import com.fivetpromart.application.dto.query.OrderSearchQuery;
import com.fivetpromart.application.port.out.ICustomerRepository;
import com.fivetpromart.application.port.out.IStaffRepository;
import com.fivetpromart.domain.model.Customer;
import com.fivetpromart.domain.model.Staff;
import com.fivetpromart.presentation.dto.query.OrderSearchQueryDto;
import com.fivetpromart.presentation.dto.request.CheckProductRequest;
import com.fivetpromart.presentation.dto.request.OrderRequest;
import com.fivetpromart.presentation.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderPresentationMapper {

    private final ICustomerRepository customerRepository;
    private final IStaffRepository staffRepository;

    // Date formatter for presentation layer
    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Map OrderDto to OrderResponse (for list view)
     */
    public OrderResponse toOrderResponse(OrderDto dto) {
        if (dto == null) return null;

        // Fetch customer name
        String customerName = "Khách lẻ";
        if (dto.getCustomerId() != null && !dto.getCustomerId().isBlank()) {
            customerName = customerRepository.findById(dto.getCustomerId())
                    .map(Customer::getFullName)
                    .orElse("Khách lẻ");
        }

        // Fetch staff name
        String staffName = staffRepository.findById(dto.getStaffId())
                .map(Staff::getFullName)
                .orElse("Unknown Staff");

        return OrderResponse.builder()
                .orderId(dto.getOrderId())
                .orderDate(formatDateTime(dto.getOrderDate()))
                .customerName(customerName)
                .staffName(staffName)
                .totalAmount(dto.getTotalAmount())
                .paymentMethod(mapPaymentMethod(dto.getPaymentMethod()))
                .status(mapStatus(dto.getStatus()))
                .createdAt(formatDateTime(dto.getOrderDate()))
                .build();
    }

    /**
     * Map OrderDto to OrderDetailResponse (for detail view)
     */
    public OrderDetailResponse toOrderDetailResponse(OrderDto dto) {
        if (dto == null) return null;

        // Fetch customer info
        OrderDetailResponse.CustomerInfo customerInfo = null;
        if (dto.getCustomerId() != null && !dto.getCustomerId().isBlank()) {
            Customer customer = customerRepository.findById(dto.getCustomerId()).orElse(null);
            if (customer != null) {
                customerInfo = OrderDetailResponse.CustomerInfo.builder()
                        .customerId(customer.getCustomerId())
                        .fullName(customer.getFullName())
                        .phoneNumber(customer.getPhoneNumber())
                        .build();
            }
        }

        // Fetch staff info
        OrderDetailResponse.StaffInfo staffInfo = null;
        Staff staff = staffRepository.findById(dto.getStaffId()).orElse(null);
        if (staff != null) {
            staffInfo = OrderDetailResponse.StaffInfo.builder()
                    .profileId(staff.getProfileId())
                    .fullName(staff.getFullName())
                    .build();
        }

        // Map items
        List<OrderDetailResponse.OrderItemInfo> items = dto.getItems().stream()
                .map(item -> OrderDetailResponse.OrderItemInfo.builder()
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subTotal(item.getSubTotal())
                        .build())
                .collect(Collectors.toList());

        return OrderDetailResponse.builder()
                .orderId(dto.getOrderId())
                .orderDate(formatDateTime(dto.getOrderDate()))
                .status(mapStatus(dto.getStatus()))
                .paymentMethod(mapPaymentMethod(dto.getPaymentMethod()))
                .customer(customerInfo)
                .staff(staffInfo)
                .items(items)
                .subTotal(dto.getSubTotal())
                .discountAmount(dto.getDiscountAmount())
                .totalAmount(dto.getTotalAmount())
                .amountGiven(dto.getAmountGiven())
                .changeReturned(dto.getChangeReturned())
                .pointsEarned(dto.getPointsEarned())
                .build();
    }

    /**
     * Map OrderDto to OrderCreationResponse
     */
    public OrderCreationResponse toOrderCreationResponse(OrderDto dto) {
        if (dto == null) return null;

        List<OrderCreationResponse.OrderItemInfo> items = dto.getItems().stream()
                .map(item -> OrderCreationResponse.OrderItemInfo.builder()
                        .productName(item.getProductName())
                        .lotId(item.getLotId())
                        .quantity(item.getQuantity())
                        .subTotal(item.getSubTotal())
                        .build())
                .collect(Collectors.toList());

        return OrderCreationResponse.builder()
                .orderId(dto.getOrderId())
                .orderDate(formatDateTime(dto.getOrderDate()))
                .totalAmount(dto.getTotalAmount())
                .changeReturned(dto.getChangeReturned())
                .pointsEarned(dto.getPointsEarned())
                .items(items)
                .build();
    }

    /**
     * Map CheckProductResultDto to CheckProductResponse
     */
    public CheckProductResponse toCheckProductResponse(CheckProductResultDto dto) {
        if (dto == null) return null;

        return CheckProductResponse.builder()
                .lotId(dto.getLotId())
                .productId(dto.getProductId())
                .productName(dto.getProductName())
                .unitOfMeasure(dto.getUnitOfMeasure())
                .unitPrice(dto.getUnitPrice())
                .quantity(dto.getQuantity())
                .subTotal(dto.getSubTotal())
                .currentStock(dto.getCurrentStock())
                .status(dto.getStatus())
                .build();
    }

    /**
     * Map OrderRequest to OrderCreationCommand
     */
    public OrderCreationCommand toOrderCreationCommand(OrderRequest request, String staffId) {
        if (request == null) return null;

        List<OrderCreationCommand.OrderItemCommand> items = request.getItems().stream()
                .map(item -> OrderCreationCommand.OrderItemCommand.builder()
                        .lotId(item.getLotId())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        // Map discount request to discount command (Polymorphism support)
        OrderCreationCommand.DiscountCommand discountCommand = null;
        if (request.getDiscount() != null) {
            discountCommand = OrderCreationCommand.DiscountCommand.builder()
                    .type(request.getDiscount().getType())
                    .percentage(request.getDiscount().getPercentage())
                    .maxAmount(request.getDiscount().getMaxAmount())
                    .amount(request.getDiscount().getAmount())
                    .pointsToUse(request.getDiscount().getPointsToUse())
                    .build();
        }

        return OrderCreationCommand.builder()
                .staffId(staffId)
                .customerId(request.getCustomerId())
                .paymentMethod(request.getPaymentMethod())
                .amountGiven(request.getAmountGiven())
                .items(items)
                .discount(discountCommand)  // NEW: Discount support
                .build();
    }

    /**
     * Map CheckProductRequest to CheckProductCommand
     */
    public CheckProductCommand toCheckProductCommand(CheckProductRequest request) {
        if (request == null) return null;

        return CheckProductCommand.builder()
                .lotId(request.getLotId())
                .quantity(request.getQuantity() != null ? request.getQuantity() : 1L)
                .build();
    }

    /**
     * Map OrderSearchQueryDto to OrderSearchQuery
     */
    public OrderSearchQuery toOrderSearchQuery(OrderSearchQueryDto dto) {
        if (dto == null) return OrderSearchQuery.builder().build();

        return OrderSearchQuery.builder()
                .search(dto.getSearch())
                .staffId(dto.getStaffId())
                .startDate(parseDate(dto.getStartDate()))
                .endDate(parseDate(dto.getEndDate()))
                .paymentMethod(dto.getPaymentMethod())
                .status(dto.getStatus())
                .build();
    }

    // Helper methods

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_TIME_FORMATTER) : null;
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    private String mapPaymentMethod(String paymentMethod) {
        if (paymentMethod == null) return null;
        switch (paymentMethod) {
            case "CASH":
                return "Tiền mặt";
            case "BANK_TRANSFER":
                return "Chuyển khoản";
            default:
                return paymentMethod;
        }
    }

    private String mapStatus(String status) {
        if (status == null) return null;
        switch (status) {
            case "PAID":
                return "Đã thanh toán";
            case "UNPAID":
                return "Chưa thanh toán";
            case "CANCELLED":
                return "Đã huỷ";
            default:
                return status;
        }
    }
}

