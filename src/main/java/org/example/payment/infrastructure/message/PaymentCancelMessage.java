package org.example.payment.infrastructure.message;

import lombok.Builder;

@Builder
public record PaymentCancelMessage(
        long transactionId,
        long orderId,
        long paymentId,
        long productId,
        long amount
) {
}
