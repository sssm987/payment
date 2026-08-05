package org.example.payment.application.order.service;

import lombok.RequiredArgsConstructor;
import org.example.payment.api.order.dto.request.OrderCreateRequestDTO;
import org.example.payment.application.order.context.OrderContext;
import org.example.payment.application.payment.PaymentService;
import org.example.payment.application.payment.cmd.PaymentCreateRequestCmd;
import org.example.payment.application.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderTransactionService {

    private final ProductService productService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    @Transactional
    public OrderContext prepareOrder(OrderCreateRequestDTO dto){
        productService.inventoryDeduction(dto.productId());
        long productPrice = productService.findProductPrice(dto.productId());
        long orderId = orderService.orderCreate(dto.memberId(),dto.productId());
        long paymentId = paymentService.paymentCreate(PaymentCreateRequestCmd.builder()
                .memberId(dto.memberId())
                .orderId(orderId)
                .fee(productPrice)
                .build());

        return new OrderContext(productPrice,orderId,paymentId);
    }
    @Transactional
    public void compensateApprovalFailure(long productId, OrderContext orderContext) {
        productService.inventoryIncrease(productId);
        orderService.orderSystemCancel(orderContext.orderId());
        paymentService.paymentFail(orderContext.paymentId());
    }
    @Transactional
    public void completePayment(OrderContext orderContext) {
        orderService.orderPaid(orderContext.orderId());
        paymentService.paymentSuccess(orderContext.paymentId());
    }
    @Transactional
    public void compensateCompletionFailure(long productId, OrderContext orderContext) {
        productService.inventoryIncrease(productId);
        orderService.orderSystemCancel(orderContext.orderId());
        paymentService.paymentSystemCancel(orderContext.paymentId());
    }
}
