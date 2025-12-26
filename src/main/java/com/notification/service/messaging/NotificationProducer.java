package com.notification.service.messaging;

import com.notification.config.NotificationConfigProperties;
import com.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final NotificationConfigProperties properties;
    private final RabbitTemplate rabbitTemplate;

    public void publish(Notification notification) {
        log.info("Publishing notification to queue: {}", notification);
        rabbitTemplate.convertAndSend(
                properties.getExchange(),
                properties.getRoutingKey(),
                notification
        );
    }
}
