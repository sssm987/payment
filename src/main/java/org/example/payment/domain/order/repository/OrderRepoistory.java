package org.example.payment.domain.order.repository;

import org.example.payment.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepoistory extends JpaRepository<Order, Long> {
}
