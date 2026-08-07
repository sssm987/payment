package org.example.payment.application.order.usecase;

import lombok.RequiredArgsConstructor;
import org.example.payment.api.order.dto.request.OrderCreateRequestDTO;
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

    public void createOrder(OrderCreateRequestDTO dto) {
        OrderContext orderContext = orderTransactionService.prepareOrder(dto);

        paymentService.paymentApprovalPublication(PaymentApproveCmd.builder()
                .orderId(orderContext.orderId())
                .paymentId(orderContext.paymentId())
                .fee(orderContext.productPrice())
                .build());

    }
}
