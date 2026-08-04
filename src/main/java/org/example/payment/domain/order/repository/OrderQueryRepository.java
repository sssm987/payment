package org.example.payment.domain.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.payment.api.order.dto.response.OrderSelectResponseDTO;
import org.example.payment.domain.order.entity.QOrder;
import org.example.payment.domain.payment.entity.QPayment;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final QOrder order = QOrder.order;
    private final QPayment payment = QPayment.payment;
    private final JPAQueryFactory queryFactory;

    public OrderSelectResponseDTO orderSelect(Long id) {
        return queryFactory
                .select(Projections.fields(
                        OrderSelectResponseDTO.class,
                        order.id.as("orderId"),
                        order.status.stringValue().as("orderStatus"),
                        payment.fee,
                        payment.status.stringValue().as("paymentStatus"),
                        payment.memberId
                ))
                .from(order)
                .leftJoin(payment).on(payment.orderId.eq(order.id))
                .where(order.id.eq(id))
                .fetchOne();
    }

}
