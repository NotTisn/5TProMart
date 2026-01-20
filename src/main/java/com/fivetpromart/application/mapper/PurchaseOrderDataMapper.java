package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.PurchaseOrderDto;
import com.fivetpromart.domain.model.PurchaseOrder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PurchaseOrderDataMapper {

    public PurchaseOrderDto toDto(PurchaseOrder domain) {
        if (domain == null) return null;

        return PurchaseOrderDto.builder()
                .id(domain.getId())
                .poCode(domain.getPoCode())
                .supplier(toSupplierInfoDto(domain.getSupplier()))
                .staffIdCreated(domain.getStaffIdCreated())
                .staffIdChecked(domain.getStaffIdChecked())
                .status(domain.getStatus())
                .notes(domain.getNotes())
                .cancellationReason(domain.getCancellationReason())
                .totalAmount(domain.getTotalAmount())
                .purchaseDate(domain.getPurchaseDate())
                .checkDate(domain.getCheckDate())
                .invoice(toInvoiceDto(domain.getInvoice()))
                .items(domain.getItems().stream()
                        .map(this::toItemDto)
                        .collect(Collectors.toList()))
                .generatedLotIds(domain.getGeneratedLotIds())
                .build();
    }

    private PurchaseOrderDto.PurchaseOrderItemDto toItemDto(PurchaseOrder.PurchaseOrderItem item) {
        if (item == null) return null;

        return PurchaseOrderDto.PurchaseOrderItemDto.builder()
                .itemId(item.getItemId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .importPrice(item.getImportPrice())
                .quantityOrdered(item.getQuantityOrdered())
                .quantityReceived(item.getQuantityReceived())
                .subTotal(item.getSubTotal())
                .build();
    }

    private PurchaseOrderDto.SupplierInfoDto toSupplierInfoDto(PurchaseOrder.SupplierInfo info) {
        if (info == null) return null;

        return PurchaseOrderDto.SupplierInfoDto.builder()
                .supplierId(info.getSupplierId())
                .supplierName(info.getSupplierName())
                .phone(info.getPhone())
                .representName(info.getRepresentName())
                .representPhoneNumber(info.getRepresentPhoneNumber())
                .build();
    }

    private PurchaseOrderDto.InvoiceDto toInvoiceDto(PurchaseOrder.Invoice invoice) {
        if (invoice == null) return null;

        return PurchaseOrderDto.InvoiceDto.builder()
                .invoiceNumber(invoice.getInvoiceNumber())
                .invoiceDate(invoice.getInvoiceDate())
                .images(invoice.getImages())
                .build();
    }

    public PurchaseOrderDto.LotToPrintDto toLotToPrintDto(PurchaseOrder.LotToPrint lot) {
        if (lot == null) return null;

        return PurchaseOrderDto.LotToPrintDto.builder()
                .lotId(lot.getLotId())
                .productName(lot.getProductName())
                .quantity(lot.getQuantity())
                .expirationDate(lot.getExpirationDate())
                .notes(lot.getNotes())
                .build();
    }
}
