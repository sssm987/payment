package org.example.payment.infrastructure.config;

import org.springframework.amqp.rabbit.config.StatelessRetryOperationsInterceptor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String PAYMENT_APPROVE_QUEUE =
            "payment.approve.queue";

    @Bean
    public Queue paymentApproveQueue() {
        return new Queue(PAYMENT_APPROVE_QUEUE, true);
    }
    @Bean
    public StatelessRetryOperationsInterceptor rabbitRetryInterceptor() {
        return RetryInterceptorBuilder
                .stateless()
                .maxRetries(2)
                .backOffOptions(
                        1000,
                        2.0,
                        5000
                )
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            StatelessRetryOperationsInterceptor rabbitRetryInterceptor
    ) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(rabbitRetryInterceptor);

        return factory;
    }
}