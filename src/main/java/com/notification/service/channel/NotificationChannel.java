package com.notification.service.channel;

import com.notification.model.Notification;
import com.notification.model.enums.NotificationType;

public interface NotificationChannel {

    void sendNotification(Notification notification);

    NotificationType getNotificationType();
}
