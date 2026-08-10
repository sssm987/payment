package org.example.payment.application.retryhistory.service;

import lombok.RequiredArgsConstructor;
import org.example.payment.domain.retryhistory.entity.RetryHistory;
import org.example.payment.domain.retryhistory.enums.RetryApiType;
import org.example.payment.domain.retryhistory.repository.RetryHistoryRepository;
import org.example.payment.global.common.DomainException;
import org.example.payment.global.common.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RetryHistoryService {

    private final RetryHistoryRepository retryHistoryRepository;
    private final ObjectMapper objectMapper;
    @Transactional
    public long retryHistoryApproveCreate(long paymentId){
        RetryHistory retryHistory = RetryHistory.create(RetryApiType.APPROVE,paymentId);

        return retryHistoryRepository.save(retryHistory).getId();
    }
    @Transactional
    public long retryHistoryCancelCreate(long paymentId){
        RetryHistory retryHistory = RetryHistory.create(RetryApiType.CANCEL,paymentId);

        return retryHistoryRepository.save(retryHistory).getId();
    }
    @Transactional
    public void retryHistorySuccess(long id){
        RetryHistory retryHistory = retryHistoryRepository.findById(id).orElseThrow(() -> new DomainException(ErrorCode.RETRY_HISTORY_NOT_FOUND));
        retryHistory.success();
    }
    @Transactional
    public void retryIncrease(long id) {
        RetryHistory retryHistory = retryHistoryRepository.findById(id).orElseThrow(() -> new DomainException(ErrorCode.RETRY_HISTORY_NOT_FOUND));
        retryHistory.increaseRetryCount();
    }
    @Transactional
    public void retryHistoryFailed(long id){
        RetryHistory retryHistory = retryHistoryRepository.findById(id).orElseThrow(() -> new DomainException(ErrorCode.RETRY_HISTORY_NOT_FOUND));
        retryHistory.fail();
    }
    public boolean isCompleted(long id){
        RetryHistory retryHistory = retryHistoryRepository.findById(id).orElseThrow(() -> new DomainException(ErrorCode.RETRY_HISTORY_NOT_FOUND));
        return retryHistory.isCompleted();
    }
}
