package org.example.payment.api.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.payment.api.order.dto.request.OrderCreateRequestDTO;
import org.example.payment.api.order.dto.response.OrderSelectResponseDTO;
import org.example.payment.application.order.service.OrderService;
import org.example.payment.application.order.usecase.OrderUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Tag(name = "주문 API")
public class OrderController {

    private final OrderService orderService;
    private final OrderUseCase orderUseCase;

    @GetMapping("{id}")
    @Operation(summary = "주문 개별 조회", description = "주문을 개별 조회합니다.")
    public OrderSelectResponseDTO orderSelect(@PathVariable("id") Long id){
        return orderService.orderSelect(id);
    }
    @PostMapping()
    @Operation(summary = "주문 생성 및 결제", description = "주문 생성 및 결제를 합니다.")
    public void orderCreate(@RequestBody OrderCreateRequestDTO dto){
        orderUseCase.createOrder(dto);
    }
}
