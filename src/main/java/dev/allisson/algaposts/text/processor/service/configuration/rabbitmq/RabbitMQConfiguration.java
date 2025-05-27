package dev.allisson.algaposts.text.processor.service.configuration.rabbitmq;

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

    public static final String POST_QUEUE = "text-processor-service.post-processing.v1.q";
    public static final String RESULT_QUEUE = "post-service.post-processing-result.v1.q";
    public static final String POST_DLQ = POST_QUEUE.replace(".q", ".dlq");
    public static final String RESULT_DLQ = RESULT_QUEUE.replace(".q", ".dlq");

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
    Queue postQueue() {
        return QueueBuilder.durable(POST_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", POST_DLQ)
                .build();
    }

    @Bean
    Queue resultQueue() {
        return QueueBuilder.durable(RESULT_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", RESULT_DLQ)
                .build();
    }

    @Bean
    Queue postDlq() {
        return new Queue(POST_DLQ);
    }

    @Bean
    Queue resultDlq() {
        return new Queue(RESULT_DLQ);
    }

}
