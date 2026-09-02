package org.example.payment.application.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.payment.api.order.dto.request.OrderCreateRequestDTO;
import org.example.payment.application.order.cmd.OrderCreateCmd;
import org.example.payment.application.order.context.OrderContext;
import org.example.payment.application.payment.cmd.PaymentCreateRequestCmd;
import org.example.payment.application.payment.service.PaymentService;
import org.example.payment.application.product.service.ProductService;
import org.example.payment.application.retryhistory.dto.PaymentApproveRetryPayload;
import org.example.payment.application.retryhistory.service.RetryHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderTransactionService {

    private final ProductService productService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final RetryHistoryService retryHistoryService;

    @Transactional
    public void prepareOrder(OrderCreateCmd dto){
        log.info("주문 생성 시작 productId={}, memberId={}",dto.productId(),dto.memberId());
        productService.inventoryDeduction(dto.productId());

        long productPrice = productService.findProductPrice(dto.productId());
        long orderId = orderService.orderCreate(dto.memberId(),dto.productId());
        long paymentId = paymentService.paymentCreate(PaymentCreateRequestCmd.builder()
                .memberId(dto.memberId())
                .orderId(orderId)
                .fee(productPrice)
                .build());
        log.info("OutBox 이벤트 생성 orderId={}, paymentId={}",orderId,paymentId);
        retryHistoryService.retryHistoryApproveCreate(PaymentApproveRetryPayload.builder()
                .orderId(orderId)
                .productPrice(productPrice)
                .paymentId(paymentId)
                .productId(dto.productId())
                .build()
        );
        log.info("주문 생성 완료 orderId={}, paymentId={}",orderId,paymentId);
    }
    @Transactional
    public void completePayment(long orderId, long paymentId) {
        orderService.orderPaid(orderId);
        paymentService.paymentSuccess(paymentId);
    }
    @Transactional
    public void compensateCompletionFailure(long productId, long orderId, long paymentId) {
        productService.inventoryIncrease(productId);
        orderService.orderSystemCancel(orderId);
        paymentService.paymentSystemCancel(paymentId);
    }
}
