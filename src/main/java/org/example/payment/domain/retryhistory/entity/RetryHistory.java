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

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(name = "retry_count")
    private int retryCount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RetryStatus status;

    @Column(name = "api_type")
    @Enumerated(EnumType.STRING)
    @Getter
    private RetryApiType retryApiType;

    @Builder(access = AccessLevel.PRIVATE)
    public RetryHistory(RetryApiType retryApiType,long paymentId) {
        this.retryCount = 0;
        this.status = RetryStatus.READY;
        this.paymentId = paymentId;
        this.retryApiType = retryApiType;
    }
    public static RetryHistory create(RetryApiType retryApiType,long paymentId){
        return RetryHistory.builder()
                .retryApiType(retryApiType)
                .paymentId(paymentId)
                .build();
    }
    public void success(){
        this.status = RetryStatus.SUCCESS;
    }
    public void fail(){
        this.status = RetryStatus.FAILED;
    }
    public void increaseRetryCount(){
        this.retryCount++;
    }
    public boolean isCompleted(){
        return this.status.equals(RetryStatus.SUCCESS) || this.status.equals(RetryStatus.FAILED);
    }
}
