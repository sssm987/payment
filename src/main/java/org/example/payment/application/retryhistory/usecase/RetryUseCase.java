package org.example.payment.application.retryhistory.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.payment.application.order.service.OrderTransactionService;
import org.example.payment.application.payment.PaymentApiService;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.cmd.PaymentCancelCmd;
import org.example.payment.application.retryhistory.dto.PaymentApproveRetryPayload;
import org.example.payment.application.retryhistory.dto.PaymentCancelRetryPayload;
import org.example.payment.application.retryhistory.service.RetryHistoryService;
import org.example.payment.domain.retryhistory.entity.RetryHistory;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetryUseCase {

    private final RetryHistoryService retryHistoryService;
    private final PaymentApiService paymentApiService;
    private final OrderTransactionService orderTransactionService;
    private final ObjectMapper objectMapper;

    public void retryPendingRequests() {
        List<RetryHistory> targets =
                retryHistoryService.findRetryTargets();

        for (RetryHistory history : targets) {
            retry(history);
        }
    }

    private void retry(RetryHistory history) {
        try {
            switch (history.getRetryApiType()) {
                case APPROVE -> retryApprove(history);
                case CANCEL -> retryCancel(history);
            }

            retryHistoryService.retryHistorySuccess(history.getId());

        } catch (Exception e) {
            log.info("재시도 실패. historyId={}",history.getId());
            retryHistoryService.retryIncrease(history.getId());
        }
    }
    private void retryApprove(RetryHistory history) {
        PaymentApproveRetryPayload payload =
                objectMapper.readValue(
                        history.getRequestPayload(),
                        PaymentApproveRetryPayload.class
                );
        log.info("재시도 승인 api 시작. historyId={}, orderId={}, paymentId={}",history.getId(),payload.orderId(),payload.paymentId());
        paymentApiService.approve(
                PaymentApproveCmd.builder()
                        .orderId(payload.orderId())
                        .paymentId(payload.paymentId())
                        .fee(payload.productPrice())
                        .build()
        );
        log.info("재시도 승인 db 시작. historyId={}, orderId={}, paymentId={}",history.getId(),payload.orderId(),payload.paymentId());
        orderTransactionService.completePayment(
                payload.orderId(),payload.paymentId()
        );
    }
    private void retryCancel(RetryHistory history) {
        PaymentCancelRetryPayload payload =
                objectMapper.readValue(
                        history.getRequestPayload(),
                        PaymentCancelRetryPayload.class
                );
        log.info("재시도 취소 api 시작. historyId={}, orderId={}, paymentId={}",history.getId(),payload.orderId(),payload.paymentId());
        paymentApiService.cancel(
                PaymentCancelCmd.builder()
                        .transactionId(payload.transactionId())
                        .amount(payload.amount())
                        .build()
        );
        log.info("재시도 취소 db 시작. historyId={}, orderId={}, paymentId={}",history.getId(),payload.orderId(),payload.paymentId());
        orderTransactionService.compensateCompletionFailure(
                payload.productId(),
                payload.orderId(),
                payload.paymentId()
        );
    }
}
