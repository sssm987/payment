package org.example.payment.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.payment.domain.order.enums.OrderStatus;
import org.example.payment.domain.product.entity.Product;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    private Order(Long memberId,Long productId){
        this.memberId = memberId;
        this.productId = productId;
        this.status = OrderStatus.CREATED;
    }

    public static Order create(long memberId,long productId){
        return Order.builder().memberId(memberId).productId(productId).build();
    }
    public void paid(){
        this.status = OrderStatus.PAID;
    }
}
