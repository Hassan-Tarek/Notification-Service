package com.notification.service.channel;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.notification.model.Notification;
import com.notification.model.enums.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PushChannelService implements NotificationChannel {

    @Override
    public void sendNotification(Notification notification) {
        log.info("Sending Push Notification to: {}", notification.getRecipient());
        try {
            Message message = Message.builder()
                    .setToken(notification.getRecipient())
                    .setNotification(com.google.firebase.messaging.Notification.builder()
                            .setTitle(notification.getSubject())
                            .setBody(notification.getContent())
                            .build())
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Push Notification sent successfully: {}", response);
        } catch (Exception e) {
            log.error("Failed to send Push Notification to {}", notification.getRecipient(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.PUSH;
    }
}
