package com.notification.mapper;

import com.notification.dto.NotificationRequest;
import com.notification.dto.NotificationResponse;
import com.notification.model.Notification;
import com.notification.model.enums.NotificationStatus;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .recipient(notification.getRecipient())
                .message(notification.getMessage())
                .type(notification.getType())
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .sentAt(notification.getSentAt())
                .build();
    }

    public Notification mapToNotification(NotificationRequest request) {
        return Notification.builder()
                .recipient(request.recipient())
                .message(request.message())
                .type(request.type())
                .status(NotificationStatus.PENDING)
                .build();
    }
}
