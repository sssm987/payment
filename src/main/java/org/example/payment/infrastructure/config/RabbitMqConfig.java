package org.example.payment.infrastructure.config;

import org.example.payment.infrastructure.recoverer.PaymentMessageRecoverer;
import org.springframework.amqp.rabbit.config.StatelessRetryOperationsInterceptor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String PAYMENT_APPROVE_QUEUE =
            "payment.approve.queue";

    public static final String PAYMENT_CANCEL_QUEUE =
            "payment.cancel.queue";

    @Bean
    public Queue paymentApproveQueue() {
        return new Queue(PAYMENT_APPROVE_QUEUE, true);
    }
    @Bean
    public Queue paymentCancelQueue() {
        return new Queue(PAYMENT_CANCEL_QUEUE, true);
    }


    @Bean
    public JacksonJsonMessageConverter rabbitMessageConverter() {
        return new JacksonJsonMessageConverter(
                "org.example.payment.infrastructure.message"
        );
    }

    @Bean
    public StatelessRetryOperationsInterceptor rabbitRetryInterceptor(PaymentMessageRecoverer recoverer) {
        return RetryInterceptorBuilder
                .stateless()
                .maxRetries(2)
                .backOffOptions(
                        1000,
                        2.0,
                        5000
                )
                .recoverer(recoverer)
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            StatelessRetryOperationsInterceptor rabbitRetryInterceptor,
            JacksonJsonMessageConverter rabbitMessageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(rabbitMessageConverter);
        factory.setAdviceChain(rabbitRetryInterceptor);

        return factory;
    }
}