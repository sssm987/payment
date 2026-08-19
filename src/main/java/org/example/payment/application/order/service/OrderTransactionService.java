package org.example.payment.application.order.service;

import lombok.RequiredArgsConstructor;
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
public class OrderTransactionService {

    private final ProductService productService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final RetryHistoryService retryHistoryService;

    @Transactional
    public void prepareOrder(OrderCreateCmd dto){
        productService.inventoryDeduction(dto.productId());

        long productPrice = productService.findProductPrice(dto.productId());
        long orderId = orderService.orderCreate(dto.memberId(),dto.productId());
        long paymentId = paymentService.paymentCreate(PaymentCreateRequestCmd.builder()
                .memberId(dto.memberId())
                .orderId(orderId)
                .fee(productPrice)
                .build());

        retryHistoryService.retryHistoryApproveCreate(PaymentApproveRetryPayload.builder()
                .orderId(orderId)
                .productPrice(productPrice)
                .paymentId(paymentId)
                .productId(dto.productId())
                .build()
        );
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
