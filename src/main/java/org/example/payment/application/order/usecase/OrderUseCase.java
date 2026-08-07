package org.example.payment.application.order.usecase;

import lombok.RequiredArgsConstructor;
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
public class OrderUseCase {

    private final PaymentApiService paymentApiService;
    private final OrderTransactionService orderTransactionService;
    private final RetryHistoryService retryHistoryService;

    public void createOrder(OrderCreateRequestDTO dto) {
        OrderContext orderContext = orderTransactionService.prepareOrder(dto);
        PaymentApproveResponseCmd paymentApproveResponseCmd;

        try {
            paymentApproveResponseCmd = paymentApiService.approve(PaymentApproveCmd.builder()
                    .orderId(orderContext.orderId())
                    .paymentId(orderContext.paymentId())
                    .fee(orderContext.productPrice())
                    .build());
        }catch (Exception e){
            retryHistoryService.retryHistoryRetry(orderContext.retryId());
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
            try {
                paymentApiService.cancel(PaymentCancelCmd.builder()
                        .transactionId(paymentApproveResponseCmd.transactionId())
                        .amount(paymentApproveResponseCmd.amount())
                        .build());
            }catch (Exception e2){
                retryHistoryService.retryHistoryRetry(retryId);
                throw e;
            }
            retryHistoryService.retryHistorySuccess(retryId);
            orderTransactionService.compensateCompletionFailure(dto.productId(), orderContext);
            throw e;
        }
    }
}
