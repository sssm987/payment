package org.example.payment.global.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다."),
    PRODUCT_INVENTORY_SHORT(HttpStatus.CONFLICT, "PRODUCT_INVENTORY_SHORT", "상품의 수량이 부족합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PAYMENT_NOT_FOUND", "주문을 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "주문을 찾을 수 없습니다."),
    RETRY_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "요청이력을 찾을 수 없습니다."),
    PG_API_NOT_RESPONSE(HttpStatus.TOO_MANY_REQUESTS, "PG_API_NOT_RESPONSE", "PG 서버 응답이 없습니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
