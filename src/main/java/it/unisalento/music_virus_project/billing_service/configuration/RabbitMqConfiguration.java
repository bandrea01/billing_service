package it.unisalento.music_virus_project.billing_service.configuration;

import it.unisalento.music_virus_project.billing_service.messaging.keys.EventFundraisingsRoutingKeys;
import it.unisalento.music_virus_project.billing_service.messaging.keys.UserEventRoutingKeys;
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

    //exchanges
    @Value("${app.rabbitmq.user-events-exchange}")
    private String userEventsExchangeName;
    @Value("${app.rabbitmq.event-fundraising-exchange}")
    private String eventFundraisingExchangeName;
    @Value("${app.rabbitmq.contribution-events-exchange}")
    private String contributionEventsExchangeName;

    //queues
    @Value("${app.rabbitmq.user-creation-queue}")
    private String userCreationQueueName;
    @Value("${app.rabbitmq.user-approval-queue}")
    private String userApprovalQueueName;
    @Value("${app.rabbitmq.user-enable-queue}")
    private String userEnableQueueName;
    @Value("${app.rabbitmq.event-creation-queue}")
    private String eventCreationQueueName;
    @Value("${app.rabbitmq.fundraising-refund-queue}")
    private String fundraisingRefundQueueName;

    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(userEventsExchangeName, true, false);
    }
    @Bean
    public TopicExchange eventFundraisingExchange() {
        return new TopicExchange(eventFundraisingExchangeName, true, false);
    }
    @Bean
    public TopicExchange contributionEventsExchange() {return new TopicExchange(contributionEventsExchangeName, true, false);}

    // User queues
    @Bean
    public Queue userCreationQueue() {
        return QueueBuilder.durable(userCreationQueueName).build();
    }
    @Bean
    public Queue userApprovalQueue() {return QueueBuilder.durable(userApprovalQueueName).build();}
    @Bean
    public Queue userEnableQueue() {return QueueBuilder.durable(userEnableQueueName).build();}

    @Bean
    public Queue eventCreationQueue() {
        return QueueBuilder.durable(eventCreationQueueName).build();
    }
    @Bean
    public Queue fundraisingRefundQueue() {
        return QueueBuilder.durable(fundraisingRefundQueueName).build();
    }

    // Queue bindings
    @Bean
    public Binding userCreationBinding(Queue userCreationQueue, TopicExchange userEventsExchange) {
        return BindingBuilder.bind(userCreationQueue)
                .to(userEventsExchange)
                .with(UserEventRoutingKeys.USER_CREATED);
    }
    @Bean
    public Binding userApprovalBinding(Queue userApprovalQueue, TopicExchange userEventsExchange) {
        return BindingBuilder.bind(userApprovalQueue)
                .to(userEventsExchange)
                .with(UserEventRoutingKeys.USER_APPROVAL_CHANGED);
    }
    @Bean
    public Binding userEnableBinding(Queue userEnableQueue, TopicExchange userEventsExchange) {
        return BindingBuilder.bind(userEnableQueue)
                .to(userEventsExchange)
                .with(UserEventRoutingKeys.USER_ENABLED_CHANGED);
    }

    @Bean
    public Binding eventCreationBinding(Queue eventCreationQueue, TopicExchange eventFundraisingExchange) {
        return BindingBuilder.bind(eventCreationQueue)
                .to(eventFundraisingExchange)
                .with(EventFundraisingsRoutingKeys.EVENT_CREATED);
    }
    @Bean
    public Binding fundraisingRefundBinding(Queue fundraisingRefundQueue, TopicExchange eventFundraisingExchange) {
        return BindingBuilder.bind(fundraisingRefundQueue)
                .to(eventFundraisingExchange)
                .with(EventFundraisingsRoutingKeys.FUNDRAISING_REFUNDED);
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
