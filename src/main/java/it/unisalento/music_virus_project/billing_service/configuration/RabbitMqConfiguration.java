package it.unisalento.music_virus_project.billing_service.configuration;

import it.unisalento.music_virus_project.billing_service.messaging.keys.ContributionEventRoutingKeys;
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
    @Value("${app.rabbitmq.contribution-events-exchange}")
    private String contributionEventsExchangeName;

    //queues
    @Value("${app.rabbitmq.user-creation-queue}")
    private String userCreationQueueName;
    @Value("${app.rabbitmq.user-approval-queue}")
    private String userApprovalQueueName;
    @Value("${app.rabbitmq.user-enable-queue}")
    private String userEnableQueueName;
    @Value ("${app.rabbitmq.contribution-events-queue}")
    private String contributionEventsQueueName;

    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(userEventsExchangeName, true, false);
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
    public Queue contributionEventsQueue() {
        return QueueBuilder.durable(contributionEventsQueueName).build();
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
    public Binding contributionAddedBinding(Queue contributionEventsQueue, TopicExchange contributionEventsExchange) {
        return BindingBuilder.bind(contributionEventsQueue)
                .to(contributionEventsExchange)
                .with(ContributionEventRoutingKeys.CONTRIBUTION_ADDED);
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
