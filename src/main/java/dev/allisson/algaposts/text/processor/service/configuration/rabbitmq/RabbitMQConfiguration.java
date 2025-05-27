package dev.allisson.algaposts.text.processor.service.configuration.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfiguration {

    public static final String ALGAPOSTS_POST_EVENTS_V1_EXCHANGE = "algaposts.post-events.v1.e";

    public static final String TEXT_PROCESSOR_QUEUE = "text-processor-service.post-processing.v1.q";
    public static final String TEXT_PROCESSOR_RESULT_QUEUE = "post-service.post-processing-result.v1.q";

    public static final String POST_CREATED_V1_RK = "post.created.v1";
    public static final String POST_PROCESSED_V1_RK = "post.processed.v1";

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter(final ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    RabbitAdmin rabbitAdmin(final ConnectionFactory connectionFactory) {
        final RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    Exchange postEventsExchange() {
        return ExchangeBuilder.directExchange(ALGAPOSTS_POST_EVENTS_V1_EXCHANGE).build();
    }

    @Bean
    Queue textProcessorQueue() {
        return QueueBuilder.durable(TEXT_PROCESSOR_QUEUE).build();
    }

    @Bean
    Binding postCreatedBinding() {
        return BindingBuilder
                .bind(this.textProcessorQueue())
                .to((DirectExchange) this.postEventsExchange())
                .with(POST_CREATED_V1_RK);
    }

    @Bean
    Queue textProcessorResultQueue() {
        return QueueBuilder.durable(TEXT_PROCESSOR_RESULT_QUEUE).build();
    }

    @Bean
    Binding postProcessedBinding() {
        return BindingBuilder
                .bind(this.textProcessorResultQueue())
                .to((DirectExchange) this.postEventsExchange())
                .with(POST_PROCESSED_V1_RK);
    }

}
