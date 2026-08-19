package org.example.payment.application.payment.cmd;

import lombok.Builder;

@Builder
public record PaymentCancelCmd(
        long transactionId,
        long paymentId,
        long orderId,
        long productId,
        long fee
) {
}
