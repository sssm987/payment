package org.example.payment.domain.payment.enums;

public enum PaymentStatus {

    READY("대기중"),
    SUCCESS("완료"),
    FAILED("실패"),
    SYSTEM_CANCELLED("서버 오류로 인한 취소"),
    CANCELLED("취소");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
