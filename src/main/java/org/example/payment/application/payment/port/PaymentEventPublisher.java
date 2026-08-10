package org.example.payment.application.payment.port;

import org.example.payment.infrastructure.message.PaymentApproveMessage;
import org.example.payment.infrastructure.message.PaymentCancelMessage;

public interface PaymentEventPublisher {
    void publishApprove(PaymentApproveMessage message);
    void publishCancel(PaymentCancelMessage message);
}
