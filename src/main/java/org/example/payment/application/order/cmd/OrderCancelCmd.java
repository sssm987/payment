package org.example.payment.application.order.cmd;

import lombok.Builder;

@Builder
public record OrderCancelCmd(
        long transactionId,
        long amount
) {
}
