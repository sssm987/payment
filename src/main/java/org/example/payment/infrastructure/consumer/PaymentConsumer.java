package org.example.payment.infrastructure.consumer;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.cmd.PaymentCancelCmd;
import org.example.payment.application.payment.usecase.PaymentUseCase;
import org.example.payment.infrastructure.config.RabbitMqConfig;
import org.example.payment.infrastructure.message.PaymentApproveMessage;
import org.example.payment.infrastructure.message.PaymentCancelMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentUseCase paymentUseCase;

    @RabbitListener(queues = RabbitMqConfig.PAYMENT_APPROVE_QUEUE)
    public void approveConsumer(PaymentApproveMessage message) {
        paymentUseCase.approve(PaymentApproveCmd.builder()
                                .orderId(message.orderId())
                                .paymentId(message.paymentId())
                                .productId(message.productId())
                                .retryId(message.retryId())
                                .fee(message.amount())
                                .build());
    }
    @RabbitListener(queues = RabbitMqConfig.PAYMENT_CANCEL_QUEUE)
    public void cancelConsume(PaymentCancelMessage message) {
        paymentUseCase.cancel(PaymentCancelCmd.builder()
                .paymentId(message.transactionId())
                .retryId(message.retryId())
                .orderId(message.orderId())
                .productId(message.productId())
                .fee(message.amount())
                .build());
    }
}
