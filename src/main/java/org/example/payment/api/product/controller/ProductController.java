package org.example.payment.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.payment.api.product.dto.response.ProductOrdersSelectResponseDTO;
import org.example.payment.application.product.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "상품 API")
public class ProductController {

    private final ProductService productService;

    @GetMapping("{id}")
    @Operation(summary = "상품 개별 조회", description = "상품을 개별 조회합니다.")
    public ProductOrdersSelectResponseDTO productSelect(@RequestParam("id") Long id) {

        return productService.productSelect(id);
    }
}
