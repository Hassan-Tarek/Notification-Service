package com.notification.dto;

import com.notification.model.enums.NotificationStatus;
import com.notification.model.enums.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
        String id,
        String recipient,
        String subject,
        String content,
        NotificationType type,
        NotificationStatus status,
        LocalDateTime createdAt,
        LocalDateTime sentAt
) { }
