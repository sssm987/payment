package org.example.payment.application.payment.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PaymentUseCase {

    private final PaymentApiService paymentApiService;
    private final PaymentService paymentService;
    private final OrderTransactionService orderTransactionService;
    private final RetryHistoryService retryHistoryService;

    public void approve(PaymentApproveCmd cmd) {
        log.info("승인 메세지 소비"+cmd.toString());
        if(retryHistoryService.isCompleted(cmd.retryId()))
            return;
        retryHistoryService.retryIncrease(cmd.retryId());
        log.info("승인 api 호출 paymentId={}, orderId={}",cmd.paymentId(),cmd.orderId());
        paymentApiService.approve(cmd);
        try {
            log.info("승인 DB 처리 호출 paymentId={}, orderId={}",cmd.paymentId(),cmd.orderId());
            if(true)
                throw new RuntimeException();
            orderTransactionService.completePayment(
                    cmd.orderId(),
                    cmd.paymentId(),
                    cmd.retryId()
            );
        }catch(Exception e) {
            log.info("승인 DB 실패 paymentId={}, orderId={}",cmd.paymentId(),cmd.orderId());
            retryHistoryService.retryHistorySuccess(cmd.retryId());
            long retryId = retryHistoryService.retryHistoryCancelCreate(cmd.paymentId());
            log.info("취소 메세지 발행 retryId={}, paymentId={}, orderId={}",retryId,cmd.paymentId(),cmd.orderId());
            paymentService.paymentCancelPublication(PaymentCancelCmd.builder()
                    .fee(cmd.fee())
                    .paymentId(cmd.paymentId())
                    .orderId(cmd.orderId())
                    .productId(cmd.productId())
                    .retryId(retryId).build());
        }
    }
    public void cancel(PaymentCancelCmd cmd){
        log.info("취소 메세지 소비"+cmd.toString());
        if(retryHistoryService.isCompleted(cmd.retryId()))
            return;
        retryHistoryService.retryIncrease(cmd.retryId());
        log.info("취소 api 호출 retryId={}, paymentId={}, orderId={}",cmd.retryId(),cmd.paymentId(),cmd.orderId());
        paymentApiService.cancel(cmd);
        log.info("취소 DB 호출 retryId={}, paymentId={}, orderId={}",cmd.retryId(),cmd.paymentId(),cmd.orderId());
        orderTransactionService.compensateCompletionFailure(cmd.productId(),cmd.orderId(),cmd.paymentId(),cmd.retryId());
    }
}
