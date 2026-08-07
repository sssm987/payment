package org.example.payment.application.order.context;

public record OrderContext(
        long productPrice,
        long orderId,
        long paymentId,
        long retryId){

}
