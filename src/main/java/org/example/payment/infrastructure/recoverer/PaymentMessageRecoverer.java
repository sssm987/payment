package org.example.payment.infrastructure.recoverer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.example.payment.application.retryhistory.service.RetryHistoryService;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentMessageRecoverer implements MessageRecoverer {

    private final RetryHistoryService retryHistoryService;

    @Override
    public void recover(Message message, Throwable cause) {

        Object header = message.getMessageProperties()
                .getHeader("retryId");

        long retryId = ((Number) header).longValue();
        log.info("MQ 재시도 횟수 초과 retryId={}",retryId);
        retryHistoryService.retryHistoryFailed(retryId);
    }
}
