package org.example.payment.application.retryhistory.service;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.retryhistory.dto.PaymentApproveRetryPayload;
import org.example.payment.application.retryhistory.dto.PaymentCancelRetryPayload;
import org.example.payment.domain.retryhistory.entity.RetryHistory;
import org.example.payment.domain.retryhistory.enums.RetryApiType;
import org.example.payment.domain.retryhistory.enums.RetryStatus;
import org.example.payment.domain.retryhistory.repository.RetryHistoryRepository;
import org.example.payment.global.common.DomainException;
import org.example.payment.global.common.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RetryHistoryService {

    private final RetryHistoryRepository retryHistoryRepository;
    private final ObjectMapper objectMapper;
    @Transactional
    public long retryHistoryApproveCreate(long productPrice,long orderId,long paymentId){
        return create(RetryApiType.APPROVE, PaymentApproveRetryPayload.builder()
                .productPrice(productPrice)
                .orderId(orderId)
                .paymentId(paymentId)
                .build());
    }
    @Transactional
    public long retryHistoryCancelCreate(long productPrice,long orderId,long paymentId,long productId,long transactionId){
        return create(RetryApiType.CANCEL, PaymentCancelRetryPayload.builder()
                .amount(productPrice)
                .orderId(orderId)
                .paymentId(paymentId)
                .productId(productId)
                .transactionId(transactionId)
                .build());
    }
    @Transactional
    public void retryHistorySuccess(long id){
        RetryHistory retryHistory = retryHistoryRepository.findById(id).orElseThrow(() -> new DomainException(ErrorCode.RETRY_HISTORY_NOT_FOUND));
        retryHistory.success();
    }
    @Transactional
    public void retryHistoryRetry(long id){
        RetryHistory retryHistory = retryHistoryRepository.findById(id).orElseThrow(() -> new DomainException(ErrorCode.RETRY_HISTORY_NOT_FOUND));
        retryHistory.retry();
    }
    @Transactional
    public void retryIncrease(long id){
        RetryHistory retryHistory = retryHistoryRepository.findById(id).orElseThrow(() -> new DomainException(ErrorCode.RETRY_HISTORY_NOT_FOUND));
        retryHistory.increaseRetryCount();
    }
    public List<RetryHistory> findRetryTargets(){
        return retryHistoryRepository.findByStatusAndRetryCountLessThan(RetryStatus.RETRY,5);
    }
    private long create(RetryApiType apiType, Object payload) {
            String requestPayload =
                    objectMapper.writeValueAsString(payload);

            RetryHistory retryHistory = RetryHistory.create(
                    apiType,
                    requestPayload
            );

            return retryHistoryRepository.save(retryHistory).getId();
    }
}
