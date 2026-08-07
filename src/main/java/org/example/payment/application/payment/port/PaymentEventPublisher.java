package org.example.payment.application.payment.port;

import org.example.payment.infrastructure.message.PaymentApproveMessage;

public interface PaymentEventPublisher {
    void publishApprove(PaymentApproveMessage message);
}
