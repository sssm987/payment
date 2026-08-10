package org.example.payment.infrastructure.message;

import lombok.Builder;

@Builder
public record PaymentCancelMessage(
        long transactionId,
        long orderId,
        long productId,
        long retryId,
        long amount
) {
}
