package org.example.payment.domain.order.enums;

public enum OrderStatus {
    CREATED("주문 생성"),
    PAYMENT_PROCESSING("결제 진행중"),
    PAID("결제 완료"),
    CANCELLED("주문 취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
