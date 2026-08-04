package org.example.payment.domain.inventory.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.payment.domain.product.entity.Product;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inventory",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_inventory_product",
                columnNames = "product_id"
        ))
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "stock", nullable = false)
    private Long stock;

    @Column(name = "initiative_stock", nullable = false)
    private Long initiativeStock;
}
