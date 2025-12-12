package com.notification.service;

import com.notification.dto.NotificationRequest;
import com.notification.dto.NotificationResponse;
import com.notification.exception.ResourceNotFoundException;
import com.notification.mapper.NotificationMapper;
import com.notification.model.Notification;
import com.notification.model.enums.NotificationStatus;
import com.notification.model.enums.NotificationType;
import com.notification.repository.NotificationRepository;
import com.notification.service.channel.NotificationChannel;
import com.notification.service.messaging.NotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationProducer notificationProducer;
    private final Map<NotificationType, NotificationChannel> channelMap;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               NotificationMapper notificationMapper,
                               NotificationProducer notificationProducer,
                               List<NotificationChannel> channels) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.notificationProducer = notificationProducer;
        this.channelMap = channels.stream()
                .collect(Collectors.toMap(NotificationChannel::getNotificationType, Function.identity()));
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
        NotificationRequest request = notificationMapper.mapToRequest(notification);

        try {
            NotificationChannel channel = channelMap.get(request.type());
            channel.sendNotification(request);
            notification.setSentAt(LocalDateTime.now());
            notification.setStatus(NotificationStatus.SENT);
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
