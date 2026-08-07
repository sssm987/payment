package org.example.payment.application.payment.usecase;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.order.service.OrderTransactionService;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.service.PaymentApiService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentApproveUseCase {

    private final PaymentApiService paymentApiService;
    private final OrderTransactionService orderTransactionService;

    public void approve(PaymentApproveCmd cmd) {
        paymentApiService.approve(cmd);

        orderTransactionService.completePayment(
                cmd.orderId(),
                cmd.paymentId()
        );
    }
}
