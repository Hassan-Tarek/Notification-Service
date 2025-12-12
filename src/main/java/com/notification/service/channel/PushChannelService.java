package com.notification.service.channel;

import com.notification.dto.NotificationRequest;
import com.notification.model.enums.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PushChannelService implements NotificationChannel {

    @Override
    public void sendNotification(NotificationRequest request) {
        // send push notification
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.PUSH;
    }
}
