package org.example.payment.application.order.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.payment.api.order.dto.request.OrderCreateRequestDTO;
import org.example.payment.application.order.context.OrderContext;
import org.example.payment.application.order.service.OrderTransactionService;
import org.example.payment.application.payment.PaymentApiService;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.cmd.PaymentApproveResponseCmd;
import org.example.payment.application.payment.cmd.PaymentCancelCmd;
import org.example.payment.application.retryhistory.service.RetryHistoryService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderUseCase {

    private final PaymentApiService paymentApiService;
    private final OrderTransactionService orderTransactionService;
    private final RetryHistoryService retryHistoryService;


    public void createOrder(OrderCreateRequestDTO dto) {
        log.info("주문 생성 시작. memberId={}, productId={}",dto.memberId(),dto.productId());
        OrderContext orderContext = orderTransactionService.prepareOrder(dto);
        log.info("주문 생성 완료. orderId={}",orderContext.orderId());
        PaymentApproveResponseCmd paymentApproveResponseCmd;

        try {
            paymentApproveResponseCmd = paymentApiService.approve(PaymentApproveCmd.builder()
                    .orderId(orderContext.orderId())
                    .paymentId(orderContext.paymentId())
                    .fee(orderContext.productPrice())
                    .build());
            log.info("PG 승인 성공 transactionId={}",paymentApproveResponseCmd.transactionId());
        }catch (Exception e){
            retryHistoryService.retryHistoryRetry(orderContext.retryId());
            log.info("PG 승인 실패 paymentId={}",orderContext.paymentId());
            throw e;
        }

        retryHistoryService.retryHistorySuccess(orderContext.retryId());

        try {
            orderTransactionService.completePayment(orderContext);
        }catch (Exception e){
            long retryId = retryHistoryService.retryHistoryCancelCreate(orderContext.productPrice(),
                    orderContext.orderId(),
                    orderContext.paymentId(),
                    dto.productId(),
                    paymentApproveResponseCmd.transactionId());
            log.info("주문 완료 변경 실패 orderId={}",orderContext.orderId());
            try {
                paymentApiService.cancel(PaymentCancelCmd.builder()
                        .transactionId(paymentApproveResponseCmd.transactionId())
                        .amount(paymentApproveResponseCmd.amount())
                        .build());
            }catch (Exception e2){
                log.info("PG 취소 API 실패 transactionId={}",paymentApproveResponseCmd.transactionId());
                retryHistoryService.retryHistoryRetry(retryId);
                throw e;
            }
            retryHistoryService.retryHistorySuccess(retryId);
            orderTransactionService.compensateCompletionFailure(dto.productId(), orderContext);
            throw e;
        }
    }
}
