package com.notification.dto;

import com.notification.model.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record NotificationRequest(

        @NotBlank(message = "Recipient is required")
        String recipient,

        @NotBlank(message = "Subject is required")
        String subject,

        @NotBlank(message = "Content is required")
        String content,

        @NotNull(message = "Type is required")
        NotificationType type
) { }
