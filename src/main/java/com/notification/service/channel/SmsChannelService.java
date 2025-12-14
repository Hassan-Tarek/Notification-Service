package com.notification.service.channel;

import com.notification.dto.NotificationRequest;
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
    public void sendNotification(NotificationRequest request) {
        log.info("Sending Sms Notification to: {} from: {}", request.recipient(), fromNumber);
        try {
            Message message = Message.creator(
                    new PhoneNumber(request.recipient()),
                    new PhoneNumber(fromNumber),
                    request.message()
            ).create();
            log.info("Sms Notification sent successfully to {}. SID={}",
                    request.recipient(), message.getSid());
        } catch (Exception e) {
            log.error("Failed to send Sms Notification to {}", request.recipient(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.SMS;
    }
}
