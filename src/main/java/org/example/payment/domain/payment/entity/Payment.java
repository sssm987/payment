package org.example.payment.domain.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.payment.domain.payment.enums.PaymentStatus;

@Entity
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @Column(name = "id")
    @Getter
    private Long id;

    @Column(name = "order_id")
    private Long order;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "fee", nullable = false)
    private long fee;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    private Payment(long memberId, long orderId, long fee){
        this.memberId = memberId;
        this.order = orderId;
        this.fee = fee;
        this.status = PaymentStatus.READY;
    }
    public static Payment create(long memberId, long orderId, long fee){
        return Payment.builder().memberId(memberId).orderId(orderId).fee(fee).build();
    }
    public void success(){
        this.status = PaymentStatus.SUCCESS;
    }
}
