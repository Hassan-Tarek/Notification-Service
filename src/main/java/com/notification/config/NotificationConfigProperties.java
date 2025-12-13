package com.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "notification.rabbitmq")
public class NotificationConfigProperties {
    private String exchange;
    private String routingKey;
    private String queue;
}
