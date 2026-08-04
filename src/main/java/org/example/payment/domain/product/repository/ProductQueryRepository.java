package org.example.payment.domain.product.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.payment.api.product.dto.response.ProductOrdersSelectResponseDTO;
import org.example.payment.domain.inventory.entity.QInventory;
import org.example.payment.domain.order.entity.QOrder;
import org.example.payment.domain.product.entity.QProduct;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final QProduct product = QProduct.product;
    private final QOrder order = QOrder.order;
    private final QInventory inventory = QInventory.inventory;
    private final JPAQueryFactory queryFactory;

    public ProductOrdersSelectResponseDTO productSelect(Long id) {
        return queryFactory
                .select(Projections.fields(
                        ProductOrdersSelectResponseDTO.class,
                        product.id.as("productId"),
                        order.id.count().as("orderCount"),
                        inventory.stock,
                        inventory.initiativeStock
                ))
                .from(product)
                .leftJoin(order).on(order.product.eq(product))
                .leftJoin(inventory).on(inventory.product.eq(product))
                .where(product.id.eq(id))
                .groupBy(product.id, inventory.stock, inventory.initiativeStock)
                .fetchOne();
    }

}
