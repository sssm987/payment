package org.example.payment.api.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.payment.application.order.cmd.OrderCreateCmd;

public record OrderCreateRequestDTO(
        @Schema(description = "상품 번호", example = "5")
        long productId,
        @Schema(description = "회원 번호", example = "10")
        long memberId
){
    public OrderCreateCmd toCmd() {
        return OrderCreateCmd.builder()
                .memberId(memberId)
                .productId(productId)
                .build();
    }
}
