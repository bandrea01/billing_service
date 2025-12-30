package it.unisalento.music_virus_project.billing_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMqConfiguration {

    @Value("${app.rabbitmq.user-events-exchange}")
    private String userEventsExchangeName;
    @Value("${app.rabbitmq.user-events-queue}")
    private String userEventsQueueName;
    @Value ("${app.rabbitmq.contribution-events-queue}")
    private String contributionEventsQueueName;

    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(userEventsExchangeName, true, false);
    }

    @Bean
    public Queue userEventsQueue() {
        return QueueBuilder.durable(userEventsQueueName).build();
    }
    @Bean
    public Queue contributionEventsQueue() {
        return QueueBuilder.durable(contributionEventsQueueName).build();
    }

    @Bean
    public Binding userRegisteredBinding(Queue userEventsQueue, TopicExchange userEventsExchange) {
        return BindingBuilder.bind(userEventsQueue)
                .to(userEventsExchange)
                .with("user.*");
    }
    @Bean
    public Binding contributionAddedBinding(Queue contributionEventsQueue, TopicExchange userEventsExchange) {
        return BindingBuilder.bind(contributionEventsQueue)
                .to(userEventsExchange)
                .with("contribution.*");
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
