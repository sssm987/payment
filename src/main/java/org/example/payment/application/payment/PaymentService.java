package org.example.payment.application.payment;

import lombok.RequiredArgsConstructor;
import org.example.payment.application.payment.cmd.PaymentCreateRequestCmd;
import org.example.payment.domain.payment.entity.Payment;
import org.example.payment.domain.payment.repository.PaymentRepository;
import org.example.payment.global.common.DomainException;
import org.example.payment.global.common.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public long paymentCreate(PaymentCreateRequestCmd cmd){
        Payment payment = Payment.create(cmd.memberId(), cmd.orderId(), cmd.fee());
        return paymentRepository.save(payment).getId();
    }
    public void paymentSuccess(long paymentId){
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new DomainException(ErrorCode.PAYMENT_NOT_FOUND));
        payment.success();
    }
}
