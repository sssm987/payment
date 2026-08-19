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
    public void retryHistoryApproveCreate(PaymentApproveRetryPayload payload){
        create(RetryApiType.APPROVE,payload);
    }
    @Transactional
    public void retryHistoryCancelCreate(PaymentCancelRetryPayload payload){
        create(RetryApiType.CANCEL,payload);
    }
    @Transactional
    public void retryHistorySuccess(long id){
        RetryHistory retryHistory = retryHistoryRepository.findById(id).orElseThrow(() -> new DomainException(ErrorCode.RETRY_HISTORY_NOT_FOUND));
        retryHistory.success();
    }
    public List<RetryHistory> findRetryTargets(){
        return retryHistoryRepository.findByStatus(RetryStatus.READY);
    }
    private void create(RetryApiType apiType, Object payload) {
        String requestPayload =
                objectMapper.writeValueAsString(payload);

        RetryHistory retryHistory = RetryHistory.create(
                apiType,
                requestPayload
        );

        retryHistoryRepository.save(retryHistory);
    }
}
