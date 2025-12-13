package com.notification.service.messaging;

import com.notification.config.NotificationConfigProperties;
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

    public void sendNotification(String notificationId) {
        log.info("Sending notification to queue: {}", notificationId);
        rabbitTemplate.convertAndSend(properties.getExchange(), properties.getRoutingKey(), notificationId);
    }
}
