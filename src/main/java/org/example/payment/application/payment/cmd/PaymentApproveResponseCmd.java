package org.example.payment.application.payment.cmd;

import org.example.payment.domain.payment.enums.PaymentApproveStatus;

import java.time.LocalDateTime;

public record PaymentApproveResponseCmd(
        long transactionId,
        long paymentId,
        long amount,
        PaymentApproveStatus status,
        LocalDateTime approvedAt,
        String failureCode,
        String failureMessage
) {
}
