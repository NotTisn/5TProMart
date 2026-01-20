package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.PurchaseOrderDto;
import com.fivetpromart.application.dto.command.PurchaseOrderCancelCommand;
import com.fivetpromart.application.dto.command.PurchaseOrderConfirmCommand;
import com.fivetpromart.application.dto.command.PurchaseOrderCreationCommand;
import com.fivetpromart.presentation.dto.request.PurchaseOrderCancelRequest;
import com.fivetpromart.presentation.dto.request.PurchaseOrderConfirmRequest;
import com.fivetpromart.presentation.dto.request.PurchaseOrderCreationRequest;
import com.fivetpromart.presentation.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PurchaseOrderPresentationMapper {

    // =================================================================
    // REQUEST TO COMMAND
    // =================================================================

    public PurchaseOrderCreationCommand toCreationCommand(PurchaseOrderCreationRequest request) {
        return PurchaseOrderCreationCommand.builder()
                .supplierId(request.getSupplierId())
                .notes(request.getNotes())
                .items(request.getItems().stream()
                        .map(item -> PurchaseOrderCreationCommand.PurchaseOrderItemCommand.builder()
                                .productId(item.getProductId())
                                .quantityOrdered(item.getQuantityOrdered())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public PurchaseOrderConfirmCommand toConfirmCommand(PurchaseOrderConfirmRequest request) {
        return PurchaseOrderConfirmCommand.builder()
                .staffIdChecked(request.getStaffIdChecked())
                .checkDate(request.getCheckDate())
                .notes(request.getNotes())
                .invoice(request.getInvoice() != null
                        ? PurchaseOrderConfirmCommand.InvoiceCommand.builder()
                        .invoiceNumber(request.getInvoice().getInvoiceNumber())
                        .invoiceDate(request.getInvoice().getInvoiceDate())
                        .images(request.getInvoice().getImages())
                        .build()
                        : null)
                .actualItems(request.getActualItems().stream()
                        .map(item -> PurchaseOrderConfirmCommand.ActualItemCommand.builder()
                                .productId(item.getProductId())
                                .quantityReceived(item.getQuantityReceived())
                                .importPrice(item.getImportPrice())
                                .manufactureDate(item.getManufactureDate())
                                .expirationDate(item.getExpirationDate())
                                .notes(item.getNotes())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public PurchaseOrderCancelCommand toCancelCommand(PurchaseOrderCancelRequest request) {
        return PurchaseOrderCancelCommand.builder()
                .staffIdChecked(request.getStaffIdChecked())
                .checkDate(request.getCheckDate())
                .cancelNotesReason(request.getCancelNotesReason())
                .build();
    }

    // =================================================================
    // DTO TO RESPONSE
    // =================================================================

    public PurchaseOrderListResponse toListResponse(PurchaseOrderDto dto) {
        return PurchaseOrderListResponse.builder()
                .id(dto.getId())
                .poCode(dto.getPoCode())
                .supplierName(dto.getSupplier() != null ? dto.getSupplier().getSupplierName() : null)
                .staffNameCreated(dto.getStaffIdCreated()) // TODO: Fetch actual staff name
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus())
                .purchaseDate(dto.getPurchaseDate())
                .checkDate(dto.getCheckDate())
                .build();
    }

    public PurchaseOrderDetailResponse toDetailResponse(PurchaseOrderDto dto) {
        return PurchaseOrderDetailResponse.builder()
                ._id(dto.getId())
                .poCode(dto.getPoCode())
                .status(dto.getStatus())
                .notes(dto.getNotes())
                .supplier(dto.getSupplier() != null
                        ? SupplierInfoResponse.builder()
                        .supplierId(dto.getSupplier().getSupplierId())
                        .supplierName(dto.getSupplier().getSupplierName())
                        .phone(dto.getSupplier().getPhone())
                        .representName(dto.getSupplier().getRepresentName())
                        .representPhoneNumber(dto.getSupplier().getRepresentPhoneNumber())
                        .build()
                        : null)
                .staffIdCreated(dto.getStaffIdCreated())
                .purchaseDate(dto.getPurchaseDate())
                .staffIdChecked(dto.getStaffIdChecked())
                .checkDate(dto.getCheckDate())
                .invoice(dto.getInvoice() != null
                        ? InvoiceResponse.builder()
                        .invoiceNumber(dto.getInvoice().getInvoiceNumber())
                        .invoiceDate(dto.getInvoice().getInvoiceDate())
                        .images(dto.getInvoice().getImages())
                        .build()
                        : null)
                .items(dto.getItems().stream()
                        .map(item -> PurchaseOrderDetailResponse.PurchaseOrderItemResponse.builder()
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .importPrice(item.getImportPrice())
                                .quantityOrdered(item.getQuantityOrdered())
                                .quantityReceived(item.getQuantityReceived())
                                .subTotal(item.getSubTotal())
                                .build())
                        .collect(Collectors.toList()))
                .totalAmount(dto.getTotalAmount())
                .generatedLotIds(dto.getGeneratedLotIds())
                .build();
    }

    public PurchaseOrderCreationResponse toCreationResponse(PurchaseOrderDto dto) {
        return PurchaseOrderCreationResponse.builder()
                .id(dto.getId())
                .poCode(dto.getPoCode())
                .supplierName(dto.getSupplier() != null ? dto.getSupplier().getSupplierName() : null)
                .status(dto.getStatus())
                .purchaseDate(dto.getPurchaseDate())
                .build();
    }

    public PurchaseOrderCancelResponse toCancelResponse(PurchaseOrderDto dto) {
        return PurchaseOrderCancelResponse.builder()
                .poCode(dto.getPoCode())
                .poId(dto.getId())
                .status(dto.getStatus())
                .cancellationReason(dto.getCancellationReason())
                .checkDate(dto.getCheckDate())
                .build();
    }

    public LotToPrintResponse toLotToPrintResponse(PurchaseOrderDto.LotToPrintDto dto) {
        return LotToPrintResponse.builder()
                .lotId(dto.getLotId())
                .productName(dto.getProductName())
                .quantity(dto.getQuantity())
                .expirationDate(dto.getExpirationDate())
                .notes(dto.getNotes())
                .build();
    }
}
