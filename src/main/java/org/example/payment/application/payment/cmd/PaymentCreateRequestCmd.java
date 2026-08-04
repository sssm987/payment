package org.example.payment.application.payment.cmd;

import lombok.Builder;

@Builder
public record PaymentCreateRequestCmd(
        long orderId,
        long memberId,
        long fee
) {
}
