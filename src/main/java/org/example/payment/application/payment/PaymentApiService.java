package org.example.payment.application.payment;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.cmd.PaymentApproveRequestCmd;
import org.example.payment.application.payment.cmd.PaymentApproveResponseCmd;
import org.example.payment.global.common.DomainException;
import org.example.payment.global.common.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class PaymentApiService {

    private final WebClient webClientPg;

    public PaymentApproveResponseCmd approve(PaymentApproveCmd cmd) {
        PaymentApproveRequestCmd request = PaymentApproveRequestCmd.builder()
                .orderId(cmd.orderId())
                .paymentId(cmd.paymentId())
                .amount(cmd.fee())
                .build();

        PaymentApproveResponseCmd response = webClientPg.post()
                .uri("/api/v1/pg/approve")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PaymentApproveResponseCmd.class)
                .block();

        if (response == null) {
            throw new DomainException(ErrorCode.PG_API_NOT_RESPONSE);
        }

        return response;
    }


}
