package org.example.payment.application.payment.cmd;

import lombok.Builder;

@Builder
public record PaymentCancelRequestCmd(
        long transactionId,
        long amount
) {
}
