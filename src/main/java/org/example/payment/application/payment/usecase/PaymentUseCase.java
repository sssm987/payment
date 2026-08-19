package org.example.payment.application.payment.usecase;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.order.cmd.OrderCancelCmd;
import org.example.payment.application.order.service.OrderTransactionService;
import org.example.payment.application.order.usecase.OrderUseCase;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.cmd.PaymentApproveResponseCmd;
import org.example.payment.application.payment.cmd.PaymentCancelCmd;
import org.example.payment.application.payment.service.PaymentApiService;
import org.example.payment.application.payment.service.PaymentService;
import org.example.payment.application.retryhistory.dto.PaymentCancelRetryPayload;
import org.example.payment.application.retryhistory.service.RetryHistoryService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentUseCase {

    private final PaymentApiService paymentApiService;
    private final OrderTransactionService orderTransactionService;
    private final RetryHistoryService retryHistoryService;

    public void approve(PaymentApproveCmd cmd) {
        PaymentApproveResponseCmd responseCmd = paymentApiService.approve(cmd);
        try {
            orderTransactionService.completePayment(
                    cmd.orderId(),
                    cmd.paymentId()
            );
        }catch(Exception e) {
            retryHistoryService.retryHistoryCancelCreate(PaymentCancelRetryPayload.builder()
                    .transactionId(responseCmd.transactionId())
                    .amount(responseCmd.amount())
                    .orderId(cmd.orderId())
                    .productId(cmd.productId())
                    .paymentId(cmd.paymentId())
                    .build());
        }
    }
    public void cancel(PaymentCancelCmd cmd){
        paymentApiService.cancel(cmd);
        orderTransactionService.compensateCompletionFailure(cmd.productId(),cmd.orderId(),cmd.paymentId());
    }
}
