package org.example.payment.application.order.service;

import lombok.RequiredArgsConstructor;
import org.example.payment.api.order.dto.request.OrderCreateRequestDTO;
import org.example.payment.application.order.cmd.OrderCreateCmd;
import org.example.payment.application.order.context.OrderContext;
import org.example.payment.application.payment.cmd.PaymentCreateRequestCmd;
import org.example.payment.application.payment.service.PaymentService;
import org.example.payment.application.product.service.ProductService;
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
    public OrderContext prepareOrder(OrderCreateCmd dto){
        productService.inventoryDeduction(dto.productId());
        long productPrice = productService.findProductPrice(dto.productId());
        long orderId = orderService.orderCreate(dto.memberId(),dto.productId());
        long paymentId = paymentService.paymentCreate(PaymentCreateRequestCmd.builder()
                .memberId(dto.memberId())
                .orderId(orderId)
                .fee(productPrice)
                .build());
        long retryId = retryHistoryService.retryHistoryApproveCreate(paymentId);

        return new OrderContext(productPrice,orderId,paymentId,retryId,dto.productId());
    }
    @Transactional
    public void completePayment(long orderId, long paymentId, long retryId) {
        orderService.orderPaid(orderId);
        paymentService.paymentSuccess(paymentId);
        retryHistoryService.retryHistorySuccess(retryId);
    }
    @Transactional
    public void compensateCompletionFailure(long productId, long orderId, long paymentId,long retryId) {
        productService.inventoryIncrease(productId);
        orderService.orderSystemCancel(orderId);
        retryHistoryService.retryHistorySuccess(retryId);
        paymentService.paymentSystemCancel(paymentId);
    }
}
