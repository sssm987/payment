package org.example.payment.infrastructure.message;

import lombok.Builder;

@Builder
public record PaymentApproveMessage(
        long orderId,
        long paymentId,
        long productId,
        long amount
) {
}
