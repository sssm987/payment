package org.example.payment.application.order.usecase;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.order.cmd.OrderCreateCmd;
import org.example.payment.application.order.context.OrderContext;
import org.example.payment.application.order.service.OrderTransactionService;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.service.PaymentService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderUseCase {

    private final PaymentService paymentService;
    private final OrderTransactionService orderTransactionService;

    public void createOrder(OrderCreateCmd cmd) {
        OrderContext orderContext = orderTransactionService.prepareOrder(cmd);

        paymentService.paymentApprovalPublication(PaymentApproveCmd.builder()
                .orderId(orderContext.orderId())
                .paymentId(orderContext.paymentId())
                .productId(orderContext.productId())
                .fee(orderContext.productPrice())
                .retryId(orderContext.retryId())
                .build());

    }
}
