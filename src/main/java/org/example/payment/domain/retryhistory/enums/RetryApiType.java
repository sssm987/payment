package org.example.payment.domain.retryhistory.enums;

public enum RetryApiType {

    APPROVE("승인"),
    CANCEL("취소");

    private final String description;

    RetryApiType(String description){
        this.description = description;
    }
}
