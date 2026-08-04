package org.example.payment.api.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class OrderSelectResponseDTO {
    @Schema(description = "주문 번호", example = "1", hidden = false)
    private Long orderId;
    @Schema(description = "주문 상태", example = "1", hidden = false)
    private String orderStatus;
    @Schema(description = "결제 금액", example = "1", hidden = false)
    private Long fee;
    @Schema(description = "결제 상태", example = "1", hidden = false)
    private String paymentStatus;
    @Schema(description = "회원 번호", example = "1", hidden = false)
    private Long memberId;
}
