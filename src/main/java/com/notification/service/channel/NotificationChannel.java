package com.notification.service.channel;

import com.notification.dto.NotificationRequest;
import com.notification.model.enums.NotificationType;

public interface NotificationChannel {

    void sendNotification(NotificationRequest request);

    NotificationType getNotificationType();
}
