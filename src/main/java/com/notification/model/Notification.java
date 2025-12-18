package com.notification.model;

import com.notification.model.enums.NotificationStatus;
import com.notification.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "notifications")
public class Notification {

    @Id
    private String id;

    private String recipient;

    private String subject;

    private String content;

    private NotificationType type;

    private NotificationStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime sentAt;
}
