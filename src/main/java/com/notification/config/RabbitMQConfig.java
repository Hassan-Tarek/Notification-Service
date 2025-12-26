package com.notification.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final NotificationConfigProperties properties;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(properties.getExchange());
    }

    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(properties.getQueue())
                .deadLetterExchange(properties.getExchange())
                .deadLetterRoutingKey(properties.getRetryRoutingKey())
                .build();
    }

    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable(properties.getRetryQueue())
                .ttl(properties.getRetry().getTtl())
                .deadLetterExchange(properties.getExchange())
                .deadLetterRoutingKey(properties.getRoutingKey())
                .build();
    }

    @Bean
    public Queue dlq() {
        return QueueBuilder.durable(properties.getDlq())
                .build();
    }

    @Bean
    public Binding binding(Queue mainQueue, TopicExchange exchange) {
        return BindingBuilder.bind(mainQueue)
                .to(exchange)
                .with(properties.getRoutingKey());
    }

    @Bean
    public Binding retryBinding(Queue retryQueue, TopicExchange exchange) {
        return BindingBuilder.bind(retryQueue)
                .to(exchange)
                .with(properties.getRetryRoutingKey());
    }

    @Bean
    public Binding dlqBinding(Queue dlq, TopicExchange exchange) {
        return BindingBuilder.bind(dlq)
                .to(exchange)
                .with(properties.getDlqRoutingKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
