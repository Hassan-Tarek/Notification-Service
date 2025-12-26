package com.notification.service.channel;

import com.notification.model.Notification;
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
    public void sendNotification(Notification notification) {
        log.info("Sending email notification to: {}", notification.getRecipient());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notification.getRecipient());
            message.setSubject(notification.getSubject());
            message.setText(notification.getContent());
            mailSender.send(message);
            log.info("Email Notification sent successfully to {}", notification.getRecipient());
        } catch (Exception e) {
            log.error("Failed to send Email Notification to {}", notification.getRecipient(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.EMAIL;
    }
}
