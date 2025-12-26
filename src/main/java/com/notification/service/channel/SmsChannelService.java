package com.notification.service.channel;

import com.notification.model.Notification;
import com.notification.model.enums.NotificationType;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsChannelService implements NotificationChannel {

    @Value("${twilio.from-number}")
    private String fromNumber;

    @Override
    public void sendNotification(Notification notification) {
        log.info("Sending Sms Notification to: {} from: {}", notification.getRecipient(), fromNumber);
        try {
            Message message = Message.creator(
                    new PhoneNumber(notification.getRecipient()),
                    new PhoneNumber(fromNumber),
                    notification.getSubject() + "\n" + notification.getContent()
            ).create();
            log.info("Sms Notification sent successfully to {}. SID={}",
                    notification.getRecipient(), message.getSid());
        } catch (Exception e) {
            log.error("Failed to send Sms Notification to {}", notification.getRecipient(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.SMS;
    }
}
