package org.example.payment.api.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderCreateRequestDTO(
        @Schema(description = "상품 번호", example = "5")
        long productId,
        @Schema(description = "회원 번호", example = "10")
        long memberId
){
}
