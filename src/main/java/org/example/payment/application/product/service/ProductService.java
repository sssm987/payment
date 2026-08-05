package org.example.payment.application.product.service;

import lombok.RequiredArgsConstructor;
import org.example.payment.api.product.dto.response.ProductOrdersSelectResponseDTO;
import org.example.payment.domain.inventory.repository.InventoryRepository;
import org.example.payment.domain.product.repository.ProductQueryRepository;
import org.example.payment.domain.product.repository.ProductRepository;
import org.example.payment.global.common.DomainException;
import org.example.payment.global.common.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductQueryRepository productQueryRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;


    public ProductOrdersSelectResponseDTO productSelect(long id) {
        ProductOrdersSelectResponseDTO p = productQueryRepository.productSelect(id);
        return productQueryRepository.productSelect(id);
    }
    @Transactional
    public void inventoryDeduction(long productId){
        if(inventoryRepository.decrease(productId) == 0)
            throw new DomainException(ErrorCode.PRODUCT_INVENTORY_SHORT);
    }
    @Transactional
    public void inventoryIncrease(long productId){
        if(inventoryRepository.increase(productId) == 0)
            throw new DomainException(ErrorCode.PRODUCT_NOT_FOUND);
    }
    public long findProductPrice(long productId){
        return productRepository.findById(productId).orElseThrow(() -> new DomainException(ErrorCode.PRODUCT_NOT_FOUND)).getPrice();
    }
}
