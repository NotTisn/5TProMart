package com.fivetpromart.infrastructure.persistence.purchase_order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceDbo {

    @Column(name = "invoice_number")
    String invoiceNumber;

    @Column(name = "invoice_date")
    LocalDate invoiceDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "purchase_order_invoice_images", joinColumns = @JoinColumn(name = "purchase_order_id"))
    @Column(name = "image_url")
    @Builder.Default
    List<String> images = new ArrayList<>();
}
