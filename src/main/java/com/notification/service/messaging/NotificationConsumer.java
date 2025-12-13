package com.notification.service.messaging;

import com.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${notification.rabbitmq.queue}")
    public void consume(String notificationId) {
        log.info("Received notification from queue: {}", notificationId);
        notificationService.processNotification(notificationId);
    }
}
