package org.example.payment.application.retryhistory.dto;

import lombok.Builder;

@Builder
public record PaymentCancelRetryPayload(
        long transactionId,
        long amount,
        long productId,
        long paymentId,
        long orderId
) {
}
