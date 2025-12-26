package com.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "notification.rabbitmq")
public class NotificationConfigProperties {
    private String exchange;
    private String queue;
    private String retryQueue;
    private String dlq;
    private String routingKey;
    private String retryRoutingKey;
    private String dlqRoutingKey;

    private Retry retry = new Retry();

    @Data
    public static class Retry {
        private int ttl;
        private int maxRetries;
    }
}
