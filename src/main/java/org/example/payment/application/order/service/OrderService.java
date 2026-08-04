package org.example.payment.application.order.service;

import lombok.RequiredArgsConstructor;
import org.example.payment.api.order.dto.response.OrderSelectResponseDTO;
import org.example.payment.domain.order.entity.Order;
import org.example.payment.domain.order.repository.OrderQueryRepository;
import org.example.payment.domain.order.repository.OrderRepoistory;
import org.example.payment.domain.product.repository.ProductRepository;
import org.example.payment.global.common.DomainException;
import org.example.payment.global.common.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderQueryRepository orderQueryRepository;
    private final OrderRepoistory orderRepoistory;

    public OrderSelectResponseDTO orderSelect(long id) {
        return orderQueryRepository.orderSelect(id);
    }
    @Transactional
    public long orderCreate(long memberId,long productId){
        Order order = Order.create(memberId,productId);
        return orderRepoistory.save(order).getId();
    }
    public void orderPaid(long orderId){
        Order order = orderRepoistory.findById(orderId)
                .orElseThrow(() -> new DomainException(ErrorCode.ORDER_NOT_FOUND));

        order.paid();
    }
}
