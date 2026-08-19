package org.example.payment.application.retryhistory.scheduler;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.retryhistory.usecase.RetryUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetryScheduler {

    private final RetryUseCase retryUseCase;

    @Scheduled(fixedDelay = 5000)
    public void retry() {
        retryUseCase.retryPendingRequests();
    }
}
