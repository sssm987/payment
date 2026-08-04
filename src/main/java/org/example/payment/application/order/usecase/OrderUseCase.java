package org.example.payment.application.order.usecase;

import lombok.RequiredArgsConstructor;
import org.example.payment.api.order.dto.request.OrderCreateRequestDTO;
import org.example.payment.application.order.service.OrderService;
import org.example.payment.application.payment.PaymentApiService;
import org.example.payment.application.payment.PaymentService;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.cmd.PaymentApproveResponseCmd;
import org.example.payment.application.payment.cmd.PaymentCreateRequestCmd;
import org.example.payment.application.product.service.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderUseCase {

    private final ProductService productService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PaymentApiService paymentApiService;

    @Transactional
    public void createOrder(OrderCreateRequestDTO dto) {

        productService.inventoryDeduction(dto.productId());
        long productPrice = productService.findProductPrice(dto.productId());
        long orderId = orderService.orderCreate(dto.memberId(),dto.productId());
        long paymentId = paymentService.paymentCreate(PaymentCreateRequestCmd.builder()
                    .memberId(dto.memberId())
                    .orderId(orderId)
                    .fee(productPrice)
                    .build());

        PaymentApproveResponseCmd paymentApproveResponseCmd = paymentApiService.approve(PaymentApproveCmd.builder()
                .orderId(orderId)
                .paymentId(paymentId)
                .fee(productPrice)
                .build());

        orderService.orderPaid(orderId);
        paymentService.paymentSuccess(paymentId);

    }
}
