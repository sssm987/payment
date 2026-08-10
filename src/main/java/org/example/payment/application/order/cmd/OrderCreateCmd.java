package org.example.payment.application.order.cmd;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record OrderCreateCmd(
        @Schema(description = "상품 번호", example = "5")
        long productId,
        @Schema(description = "회원 번호", example = "10")
        long memberId
){
}
