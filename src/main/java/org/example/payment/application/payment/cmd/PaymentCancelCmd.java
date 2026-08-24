package org.example.payment.application.payment.cmd;

import lombok.Builder;

@Builder
public record PaymentCancelCmd(
        long paymentId,
        long orderId,
        long productId,
        long fee,
        long retryId
) {
    @Override
    public String toString() {
        return "paymentId : "+paymentId+"\norderId : "+orderId+"\nproductId : "+productId+"\nfee : "+fee;
    }
}
