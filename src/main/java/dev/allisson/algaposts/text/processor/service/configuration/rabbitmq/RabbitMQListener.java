package dev.allisson.algaposts.text.processor.service.configuration.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import dev.allisson.algaposts.text.processor.service.posts.dto.PostCreatedDto;
import dev.allisson.algaposts.text.processor.service.posts.service.TextProcessorService;

@Component
public class RabbitMQListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQListener.class);

    private final TextProcessorService textProcessorService;
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQListener(final TextProcessorService textProcessorService, final RabbitTemplate rabbitTemplate) {
        this.textProcessorService = textProcessorService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfiguration.TEXT_PROCESSOR_QUEUE)
    public void listen(final PostCreatedDto message) {
        LOGGER.info("Received message: {}", message);
        final var postProcessedDto = this.textProcessorService.processPost(message);
        LOGGER.debug("Processed message: {}", postProcessedDto);
        this.rabbitTemplate.convertAndSend(
                RabbitMQConfiguration.ALGAPOSTS_POST_EVENTS_V1_EXCHANGE,
                RabbitMQConfiguration.POST_PROCESSED_V1_RK,
                postProcessedDto);
        LOGGER.debug("Sent message: {}", postProcessedDto);
        LOGGER.info("Message processed and sent successfully.");
    }

}
