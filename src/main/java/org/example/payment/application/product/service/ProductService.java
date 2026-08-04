package org.example.payment.application.product.service;

import lombok.RequiredArgsConstructor;
import org.example.payment.api.product.dto.response.ProductOrdersSelectResponseDTO;
import org.example.payment.domain.product.repository.ProductQueryRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductQueryRepository productQueryRepository;

    public ProductOrdersSelectResponseDTO productSelect(long id) {
        ProductOrdersSelectResponseDTO p = productQueryRepository.productSelect(id);
        return productQueryRepository.productSelect(id);
    }
}
