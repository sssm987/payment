package org.example.payment.domain.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.payment.domain.order.entity.Order;
import org.example.payment.domain.payment.enums.PaymentStatus;

@Entity
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "fee", nullable = false)
    private Long fee;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
