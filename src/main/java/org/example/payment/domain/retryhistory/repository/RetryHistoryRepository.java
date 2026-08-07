package org.example.payment.domain.retryhistory.repository;

import org.example.payment.domain.retryhistory.entity.RetryHistory;
import org.example.payment.domain.retryhistory.enums.RetryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetryHistoryRepository extends JpaRepository<RetryHistory,Long> {
    List<RetryHistory> findByStatusAndRetryCountLessThan(RetryStatus status, int retryCount);
}
