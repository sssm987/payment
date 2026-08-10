package org.example.payment.application.payment.service;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.payment.cmd.PaymentApproveCmd;
import org.example.payment.application.payment.cmd.PaymentCancelCmd;
import org.example.payment.application.payment.cmd.PaymentCreateRequestCmd;
import org.example.payment.application.payment.port.PaymentEventPublisher;
import org.example.payment.domain.payment.entity.Payment;
import org.example.payment.domain.payment.repository.PaymentRepository;
import org.example.payment.global.common.DomainException;
import org.example.payment.global.common.ErrorCode;
import org.example.payment.infrastructure.message.PaymentApproveMessage;
import org.example.payment.infrastructure.message.PaymentCancelMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;

    public long paymentCreate(PaymentCreateRequestCmd cmd){
        Payment payment = Payment.create(cmd.memberId(), cmd.orderId(), cmd.fee());
        return paymentRepository.save(payment).getId();
    }
    public void paymentSuccess(long paymentId){
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new DomainException(ErrorCode.PAYMENT_NOT_FOUND));
        payment.success();
    }
    public void paymentSystemCancel(long paymentId){
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new DomainException(ErrorCode.PAYMENT_NOT_FOUND));
        payment.systemCancel();
    }
    public void paymentApprovalPublication(PaymentApproveCmd paymentApproveCmd){
        paymentEventPublisher.publishApprove(PaymentApproveMessage.builder()
                .paymentId(paymentApproveCmd.paymentId())
                .orderId(paymentApproveCmd.orderId())
                .productId(paymentApproveCmd.productId())
                .retryId(paymentApproveCmd.retryId())
                .amount(paymentApproveCmd.fee())
                .build());
    }
    public void paymentCancelPublication(PaymentCancelCmd paymentCancelCmd){
        paymentEventPublisher.publishCancel(PaymentCancelMessage.builder()
                .transactionId(paymentCancelCmd.paymentId())
                .amount(paymentCancelCmd.fee())
                .orderId(paymentCancelCmd.orderId())
                .productId(paymentCancelCmd.productId())
                .retryId(paymentCancelCmd.retryId())
                .build());
    }
}
