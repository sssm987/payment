package org.example.payment.application.order.usecase;

import lombok.RequiredArgsConstructor;
import org.example.payment.api.order.dto.request.OrderCreateRequestDTO;
import org.example.payment.application.order.context.OrderContext;
import org.example.payment.application.order.service.OrderTransactionService;
import org.example.payment.application.payment.PaymentApiService;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.cmd.PaymentApproveResponseCmd;
import org.example.payment.application.payment.cmd.PaymentCancelCmd;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderUseCase {

    private final PaymentApiService paymentApiService;
    private final OrderTransactionService orderTransactionService;

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
            orderTransactionService.compensateApprovalFailure(dto.productId(), orderContext);
            throw e;
        }
        try {
            orderTransactionService.completePayment(orderContext);
        }catch (Exception e){
            paymentApiService.cancel(PaymentCancelCmd.builder()
                    .transactionId(paymentApproveResponseCmd.transactionId())
                    .amount(paymentApproveResponseCmd.amount())
                    .build());
            orderTransactionService.compensateCompletionFailure(dto.productId(), orderContext);
            throw e;
        }
    }
}
