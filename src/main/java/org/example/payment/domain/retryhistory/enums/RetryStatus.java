package org.example.payment.domain.retryhistory.enums;

public enum RetryStatus {

    READY("대기중"),
    SUCCESS("완료");

    private final String description;

    RetryStatus(String description){
        this.description = description;
    }
}
