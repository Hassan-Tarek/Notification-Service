package com.notification.service.channel;

import com.notification.dto.NotificationRequest;
import com.notification.model.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailChannelService implements NotificationChannel {

    private final JavaMailSender mailSender;

    @Override
    public void sendNotification(NotificationRequest request) {
        log.info("Sending email notification to: {}", request.recipient());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.recipient());
            message.setSubject("Email Notification");
            message.setText(request.message());
            mailSender.send(message);
            log.info("Email Notification sent successfully to {}", request.recipient());
        } catch (Exception e) {
            log.error("Failed to send Email Notification to {}", request.recipient(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.EMAIL;
    }
}
