package org.example.payment.domain.retryhistory.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.payment.domain.retryhistory.enums.RetryApiType;
import org.example.payment.domain.retryhistory.enums.RetryStatus;

@Entity
@Table(name = "retry_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RetryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "retry_count")
    private int retryCount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RetryStatus status;

    @Column(name = "api_type")
    @Enumerated(EnumType.STRING)
    @Getter
    private RetryApiType retryApiType;

    @Column(name = "request_payload", columnDefinition = "TEXT", nullable = false)
    @Getter
    private String requestPayload;

    @Builder(access = AccessLevel.PRIVATE)
    public RetryHistory(RetryApiType retryApiType, String requestPayload) {
        this.retryCount = 0;
        this.status = RetryStatus.READY;
        this.retryApiType = retryApiType;
        this.requestPayload = requestPayload;
    }
    public static RetryHistory create(RetryApiType retryApiType, String requestPayload){
        return RetryHistory.builder()
                .retryApiType(retryApiType)
                .requestPayload(requestPayload)
                .build();
    }
    public void success(){
        this.status = RetryStatus.SUCCESS;
    }
    public void retry(){
        this.status = RetryStatus.RETRY;
    }
    public void increaseRetryCount(){
        this.retryCount++;
        if(this.retryCount >= 5){
            this.status = RetryStatus.FAILED;
        }
    }
}
