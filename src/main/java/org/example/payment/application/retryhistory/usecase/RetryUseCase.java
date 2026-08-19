package org.example.payment.application.retryhistory.usecase;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.cmd.PaymentCancelCmd;
import org.example.payment.application.payment.service.PaymentService;
import org.example.payment.application.retryhistory.dto.PaymentApproveRetryPayload;
import org.example.payment.application.retryhistory.dto.PaymentCancelRetryPayload;
import org.example.payment.application.retryhistory.service.RetryHistoryService;
import org.example.payment.domain.retryhistory.entity.RetryHistory;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RetryUseCase {

    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;
    private final RetryHistoryService retryHistoryService;

    public void retryPendingRequests() {
        List<RetryHistory> targets =
                retryHistoryService.findRetryTargets();

        for (RetryHistory history : targets) {
            retry(history);
        }
    }
    private void retry(RetryHistory history) {
        switch (history.getRetryApiType()) {
            case APPROVE -> retryApprove(history);
            case CANCEL -> retryCancel(history);
        }

        retryHistoryService.retryHistorySuccess(history.getId());
    }
    private void retryApprove(RetryHistory history) {
        PaymentApproveRetryPayload payload =
                objectMapper.readValue(
                        history.getRequestPayload(),
                        PaymentApproveRetryPayload.class
                );

        paymentService.paymentApprovalPublication(PaymentApproveCmd.builder()
                .orderId(payload.orderId())
                .paymentId(payload.paymentId())
                .productId(payload.productId())
                .fee(payload.productPrice())
                .build());
    }
    private void retryCancel(RetryHistory history) {
        PaymentCancelRetryPayload payload =
                objectMapper.readValue(
                        history.getRequestPayload(),
                        PaymentCancelRetryPayload.class
                );

        paymentService.paymentCancelPublication(PaymentCancelCmd.builder()
                .orderId(payload.orderId())
                .transactionId(payload.transactionId())
                .paymentId(payload.paymentId())
                .productId(payload.productId())
                .fee(payload.amount())
                .build()
        );

    }
}
