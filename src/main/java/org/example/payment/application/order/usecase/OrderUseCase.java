package org.example.payment.application.order.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OrderUseCase {

    private final ProductService productService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PaymentApiService paymentApiService;

    @Transactional
    public void createOrder(OrderCreateRequestDTO dto) {
        log.info("주문 생성 시작. memberId={}, productId={}",dto.memberId(),dto.productId());
        productService.inventoryDeduction(dto.productId());
        log.info("재고 차감 완료");
        long productPrice = productService.findProductPrice(dto.productId());
        long orderId = orderService.orderCreate(dto.memberId(),dto.productId());
        log.info("주문 생성 완료. orderId={}",orderId);
        long paymentId = paymentService.paymentCreate(PaymentCreateRequestCmd.builder()
                    .memberId(dto.memberId())
                    .orderId(orderId)
                    .fee(productPrice)
                    .build());
        log.info("결제 생성 완료. paymentId={}",paymentId);
        PaymentApproveResponseCmd paymentApproveResponseCmd = paymentApiService.approve(PaymentApproveCmd.builder()
                .orderId(orderId)
                .paymentId(paymentId)
                .fee(productPrice)
                .build());
        log.info("PG 승인 성공 transactionId={}",paymentApproveResponseCmd.transactionId());
        orderService.orderPaid(orderId);
        log.info("주문 상태 변경 완료");
        paymentService.paymentSuccess(paymentId);
        log.info("결제 상태 변경 완료");
    }
}
