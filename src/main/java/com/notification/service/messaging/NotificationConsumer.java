package com.notification.service.messaging;

import com.notification.config.NotificationConfigProperties;
import com.notification.model.Notification;
import com.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final NotificationConfigProperties properties;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "${notification.rabbitmq.queue}")
    public void consume(Notification notification,
                        @Header(name = "x-death", required = false) List<Map<String, Object>> xDeath,
                        Message message) {
        int retryCount = 0;
        if (xDeath != null && !xDeath.isEmpty()) {
            retryCount = ((Long) xDeath.get(0).get("count")).intValue();
        }

        log.info("Consuming notification from queue: {} | retry count: {}", notification, retryCount);

        // Max retry check
        if (retryCount >= properties.getRetry().getMaxRetries()) {
            log.warn("Max retry attempts reached. Sending to DLQ: {}", notification);

            // Send the original AMQP Message to DLQ (preserve headers)
            rabbitTemplate.send(
                    properties.getExchange(),
                    properties.getDlqRoutingKey(),
                    message);
            return;
        }

        try {
            notificationService.processNotification(notification);
        } catch (Exception e) {
            // Throw to trigger retry (message goes to retry queue automatically)
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = "${notification.rabbitmq.dlq}")
    public void consumeDlq(Message message) {
        log.error("Consuming dlq message: {}", message);
    }
}
