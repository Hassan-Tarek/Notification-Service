package com.notification.service.channel;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.notification.dto.NotificationRequest;
import com.notification.model.enums.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PushChannelService implements NotificationChannel {

    @Override
    public void sendNotification(NotificationRequest request) {
        log.info("Sending Push Notification to: {}", request.recipient());
        try {
            Message message = Message.builder()
                    .setToken(request.recipient())
                    .setNotification(Notification.builder()
                            .setTitle(request.subject())
                            .setBody(request.content())
                            .build())
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Push Notification sent successfully: {}", response);
        } catch (Exception e) {
            log.error("Failed to send Push Notification to {}", request.recipient(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.PUSH;
    }
}
