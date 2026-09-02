package org.example.payment.application.payment.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PaymentUseCase {

    private final PaymentApiService paymentApiService;
    private final OrderTransactionService orderTransactionService;
    private final RetryHistoryService retryHistoryService;

    public void approve(PaymentApproveCmd cmd) {
        log.info("승인 메세지 소비 시작 orderId={}, paymentId={}",cmd.orderId(),cmd.paymentId());
        PaymentApproveResponseCmd responseCmd = paymentApiService.approve(cmd);
        try {
            orderTransactionService.completePayment(
                    cmd.orderId(),
                    cmd.paymentId()
            );
            log.info("승인 완료 orderId={}, paymentId={}",cmd.orderId(),cmd.paymentId());
        }catch(Exception e) {
            log.info("승인 DB적재 실패/이벤트 적재 orderId={}, paymentId={}",cmd.orderId(),cmd.paymentId());
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
        log.info("취소 메세지 소비 시작 orderId={}, paymentId={}",cmd.orderId(),cmd.paymentId());
        paymentApiService.cancel(cmd);
        orderTransactionService.compensateCompletionFailure(cmd.productId(),cmd.orderId(),cmd.paymentId());
        log.info("취소 메세지 소비 완료 orderId={}, paymentId={}",cmd.orderId(),cmd.paymentId());
    }
}
