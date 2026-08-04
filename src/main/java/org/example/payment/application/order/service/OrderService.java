package org.example.payment.application.order.service;

import lombok.RequiredArgsConstructor;
import org.example.payment.api.order.dto.response.OrderSelectResponseDTO;
import org.example.payment.domain.order.repository.OrderQueryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderQueryRepository orderQueryRepository;

    public OrderSelectResponseDTO orderSelect(long id) {
        return orderQueryRepository.orderSelect(id);
    }
}
