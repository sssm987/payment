package org.example.payment.api.order.dto.request;

public record OrderCreateRequestDTO(
        long productId,
        long memberId
){
}
