package org.example.payment.application.payment.cmd;

import lombok.Builder;

@Builder
public record PaymentCancelCmd(
        long transactionId,
        long amount
) {
}
