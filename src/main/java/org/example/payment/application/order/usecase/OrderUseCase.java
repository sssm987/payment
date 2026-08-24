package org.example.payment.application.order.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.payment.application.order.cmd.OrderCreateCmd;
import org.example.payment.application.order.context.OrderContext;
import org.example.payment.application.order.service.OrderTransactionService;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.service.PaymentService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderUseCase {

    private final PaymentService paymentService;
    private final OrderTransactionService orderTransactionService;

    public void createOrder(OrderCreateCmd cmd) {
        OrderContext orderContext = orderTransactionService.prepareOrder(cmd);
        log.info("승인 메세지 발행 paymentId={}, orderId={}",orderContext.paymentId(),orderContext.orderId());
        paymentService.paymentApprovalPublication(PaymentApproveCmd.builder()
                .orderId(orderContext.orderId())
                .paymentId(orderContext.paymentId())
                .productId(orderContext.productId())
                .fee(orderContext.productPrice())
                .retryId(orderContext.retryId())
                .build());

    }
}
