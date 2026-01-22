package com.fivetpromart.infrastructure.persistence.purchase_order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderDbo {

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "po_code", unique = true)
    String poCode;

    @Embedded
    SupplierInfoDbo supplier;

    @Column(name = "staff_id_created")
    String staffIdCreated;

    @Column(name = "staff_id_checked")
    String staffIdChecked;

    @Column(name = "status")
    String status;

    @Column(name = "notes", columnDefinition = "TEXT")
    String notes;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    String cancellationReason;

    @Column(name = "total_amount", precision = 15, scale = 2)
    BigDecimal totalAmount;

    @Column(name = "purchase_date")
    LocalDate purchaseDate;

    @Column(name = "check_date")
    LocalDate checkDate;

    @Embedded
    InvoiceDbo invoice;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    List<PurchaseOrderItemDbo> items = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "purchase_order_lots", joinColumns = @JoinColumn(name = "purchase_order_id"))
    @Column(name = "lot_id")
    @Builder.Default
    List<String> generatedLotIds = new ArrayList<>();

    public void addItem(PurchaseOrderItemDbo item) {
        items.add(item);
        item.setPurchaseOrder(this);
    }

    public void removeItem(PurchaseOrderItemDbo item) {
        items.remove(item);
        item.setPurchaseOrder(null);
    }
}
