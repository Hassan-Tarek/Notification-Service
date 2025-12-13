package com.notification.service;

import com.notification.dto.NotificationRequest;
import com.notification.dto.NotificationResponse;
import com.notification.exception.ResourceNotFoundException;
import com.notification.mapper.NotificationMapper;
import com.notification.model.Notification;
import com.notification.model.enums.NotificationStatus;
import com.notification.repository.NotificationRepository;
import com.notification.service.messaging.NotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationProducer notificationProducer;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               NotificationMapper notificationMapper,
                               NotificationProducer notificationProducer) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.notificationProducer = notificationProducer;
    }

    public NotificationResponse initializeNotification(NotificationRequest request) {
        Notification notification = notificationMapper.mapToNotification(request);
        notificationRepository.save(notification);

        // Publish the notification id to RabbitMQ
        notificationProducer.sendNotification(notification.getId());

        return notificationMapper.mapToResponse(notification);
    }

    public void processNotification(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found!"));

        try {
            // Process notification
            Thread.sleep(1000);
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
        }
        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(notificationMapper::mapToResponse)
                .toList();
    }

    public NotificationResponse getNotification(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found!"));
        return notificationMapper.mapToResponse(notification);
    }
}
