package com.fivetpromart.infrastructure.persistence.purchase_order.mapper;

import com.fivetpromart.domain.model.PurchaseOrder;
import com.fivetpromart.infrastructure.persistence.purchase_order.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PurchaseOrderPersistenceMapper {

    public PurchaseOrderDbo toDbo(PurchaseOrder domain) {
        if (domain == null) return null;

        PurchaseOrderDbo dbo = PurchaseOrderDbo.builder()
                .id(domain.getId())
                .poCode(domain.getPoCode())
                .supplier(toSupplierInfoDbo(domain.getSupplier()))
                .staffIdCreated(domain.getStaffIdCreated())
                .staffIdChecked(domain.getStaffIdChecked())
                .status(domain.getStatus())
                .notes(domain.getNotes())
                .cancellationReason(domain.getCancellationReason())
                .totalAmount(domain.getTotalAmount())
                .purchaseDate(domain.getPurchaseDate())
                .checkDate(domain.getCheckDate())
                .invoice(toInvoiceDbo(domain.getInvoice()))
                .generatedLotIds(domain.getGeneratedLotIds())
                .build();

        // Map items
        if (domain.getItems() != null) {
            for (PurchaseOrder.PurchaseOrderItem item : domain.getItems()) {
                PurchaseOrderItemDbo itemDbo = toItemDbo(item);
                dbo.addItem(itemDbo);
            }
        }

        return dbo;
    }

    public PurchaseOrder toDomain(PurchaseOrderDbo dbo) {
        if (dbo == null) return null;

        return PurchaseOrder.reconstitute(
                dbo.getId(),
                dbo.getPoCode(),
                toSupplierInfoDomain(dbo.getSupplier()),
                dbo.getStaffIdCreated(),
                dbo.getStaffIdChecked(),
                dbo.getStatus(),
                dbo.getNotes(),
                dbo.getCancellationReason(),
                dbo.getTotalAmount(),
                dbo.getPurchaseDate(),
                dbo.getCheckDate(),
                toInvoiceDomain(dbo.getInvoice()),
                dbo.getItems().stream()
                        .map(this::toItemDomain)
                        .collect(Collectors.toList()),
                dbo.getGeneratedLotIds()
        );
    }

    private PurchaseOrderItemDbo toItemDbo(PurchaseOrder.PurchaseOrderItem item) {
        if (item == null) return null;

        return PurchaseOrderItemDbo.builder()
                .itemId(item.getItemId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .importPrice(item.getImportPrice())
                .quantityOrdered(item.getQuantityOrdered())
                .quantityReceived(item.getQuantityReceived())
                .subTotal(item.getSubTotal())
                .build();
    }

    private PurchaseOrder.PurchaseOrderItem toItemDomain(PurchaseOrderItemDbo dbo) {
        if (dbo == null) return null;

        return PurchaseOrder.PurchaseOrderItem.reconstitute(
                dbo.getItemId(),
                dbo.getPurchaseOrder() != null ? dbo.getPurchaseOrder().getId() : null,
                dbo.getProductId(),
                dbo.getProductName(),
                dbo.getImportPrice(),
                dbo.getQuantityOrdered(),
                dbo.getQuantityReceived(),
                dbo.getSubTotal()
        );
    }

    private SupplierInfoDbo toSupplierInfoDbo(PurchaseOrder.SupplierInfo info) {
        if (info == null) return null;

        return SupplierInfoDbo.builder()
                .supplierId(info.getSupplierId())
                .supplierName(info.getSupplierName())
                .phone(info.getPhone())
                .representName(info.getRepresentName())
                .representPhoneNumber(info.getRepresentPhoneNumber())
                .build();
    }

    private PurchaseOrder.SupplierInfo toSupplierInfoDomain(SupplierInfoDbo dbo) {
        if (dbo == null) return null;

        return PurchaseOrder.SupplierInfo.reconstitute(
                dbo.getSupplierId(),
                dbo.getSupplierName(),
                dbo.getPhone(),
                dbo.getRepresentName(),
                dbo.getRepresentPhoneNumber()
        );
    }

    private InvoiceDbo toInvoiceDbo(PurchaseOrder.Invoice invoice) {
        if (invoice == null) return null;

        return InvoiceDbo.builder()
                .invoiceNumber(invoice.getInvoiceNumber())
                .invoiceDate(invoice.getInvoiceDate())
                .images(invoice.getImages())
                .build();
    }

    private PurchaseOrder.Invoice toInvoiceDomain(InvoiceDbo dbo) {
        if (dbo == null) return null;

        return PurchaseOrder.Invoice.reconstitute(
                dbo.getInvoiceNumber(),
                dbo.getInvoiceDate(),
                dbo.getImages()
        );
    }
}
