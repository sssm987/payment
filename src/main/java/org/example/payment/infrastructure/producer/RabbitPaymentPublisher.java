package org.example.payment.infrastructure.producer;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.payment.port.PaymentEventPublisher;
import org.example.payment.infrastructure.config.RabbitMqConfig;
import org.example.payment.infrastructure.message.PaymentApproveMessage;
import org.example.payment.infrastructure.message.PaymentCancelMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitPaymentPublisher implements PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishApprove(PaymentApproveMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.PAYMENT_APPROVE_QUEUE,
                message
        );
    }
    @Override
    public void publishCancel(PaymentCancelMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.PAYMENT_CANCEL_QUEUE,
                message
        );
    }
}