package org.example.payment.application.payment.cmd;

import lombok.Builder;

@Builder
public record PaymentApproveRequestCmd(
        long paymentId,
        long orderId,
        long amount
) {
}
