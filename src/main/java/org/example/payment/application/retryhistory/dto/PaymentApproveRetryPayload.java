package org.example.payment.application.retryhistory.dto;

import lombok.Builder;

@Builder
public record PaymentApproveRetryPayload(
        long productPrice,
        long orderId,
        long paymentId,
        long productId
) {
}