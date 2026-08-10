package org.example.payment.application.payment.cmd;

import lombok.Builder;

@Builder
public record PaymentApproveCmd(
        long paymentId,
        long orderId,
        long productId,
        long fee,
        long retryId
) {
}
