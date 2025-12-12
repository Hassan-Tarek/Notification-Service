package com.notification.service.channel;

import com.notification.dto.NotificationRequest;
import com.notification.model.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailChannelService implements NotificationChannel {

    @Override
    public void sendNotification(NotificationRequest request) {
        // send email notification
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.EMAIL;
    }
}
