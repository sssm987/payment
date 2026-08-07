package org.example.payment.infrastructure.consumer;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.usecase.PaymentApproveUseCase;
import org.example.payment.infrastructure.config.RabbitMqConfig;
import org.example.payment.infrastructure.message.PaymentApproveMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentApproveConsumer {

    private final PaymentApproveUseCase paymentApproveUseCase;

    @RabbitListener(queues = RabbitMqConfig.PAYMENT_APPROVE_QUEUE)
    public void consume(PaymentApproveMessage message) {
        paymentApproveUseCase.approve(PaymentApproveCmd.builder()
                                .orderId(message.orderId())
                                .paymentId(message.paymentId())
                                .fee(message.amount())
                                .build());
    }
}
