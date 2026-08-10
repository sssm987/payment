package org.example.payment.application.payment.usecase;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.order.cmd.OrderCancelCmd;
import org.example.payment.application.order.service.OrderTransactionService;
import org.example.payment.application.order.usecase.OrderUseCase;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.cmd.PaymentCancelCmd;
import org.example.payment.application.payment.service.PaymentApiService;
import org.example.payment.application.payment.service.PaymentService;
import org.example.payment.application.retryhistory.service.RetryHistoryService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentUseCase {

    private final PaymentApiService paymentApiService;
    private final PaymentService paymentService;
    private final OrderTransactionService orderTransactionService;
    private final RetryHistoryService retryHistoryService;

    public void approve(PaymentApproveCmd cmd) {

        if(retryHistoryService.isCompleted(cmd.retryId()))
            return;
        retryHistoryService.retryIncrease(cmd.retryId());
        paymentApiService.approve(cmd);
        try {
            orderTransactionService.completePayment(
                    cmd.orderId(),
                    cmd.paymentId(),
                    cmd.retryId()
            );
        }catch(Exception e) {
            retryHistoryService.retryHistorySuccess(cmd.retryId());
            long retryId = retryHistoryService.retryHistoryCancelCreate(cmd.paymentId());
            paymentService.paymentCancelPublication(PaymentCancelCmd.builder()
                    .fee(cmd.fee())
                    .paymentId(cmd.paymentId())
                    .orderId(cmd.orderId())
                    .productId(cmd.productId())
                    .retryId(retryId).build());
        }
    }
    public void cancel(PaymentCancelCmd cmd){
        if(retryHistoryService.isCompleted(cmd.retryId()))
            return;
        retryHistoryService.retryIncrease(cmd.retryId());
        paymentApiService.cancel(cmd);
        orderTransactionService.compensateCompletionFailure(cmd.productId(),cmd.orderId(),cmd.paymentId(),cmd.retryId());
    }
}
