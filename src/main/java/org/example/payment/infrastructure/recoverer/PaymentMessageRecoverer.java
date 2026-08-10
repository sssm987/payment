package org.example.payment.infrastructure.recoverer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.example.payment.application.retryhistory.service.RetryHistoryService;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMessageRecoverer implements MessageRecoverer {

    private final RetryHistoryService retryHistoryService;

    @Override
    public void recover(Message message, Throwable cause) {

        Object header = message.getMessageProperties()
                .getHeader("retryId");

        long retryId = ((Number) header).longValue();

        retryHistoryService.retryHistoryFailed(retryId);
    }
}
