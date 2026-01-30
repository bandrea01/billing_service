package it.unisalento.music_virus_project.billing_service.messaging.publish;//package it.unisalento.music_virus_project.billing_service.messaging;

import it.unisalento.music_virus_project.billing_service.messaging.dto.ContributionEventDTO;
import it.unisalento.music_virus_project.billing_service.messaging.keys.EventFundraisingsRoutingKeys;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ContributionEventPublisher {

    private static final Logger log = Logger.getLogger(ContributionEventPublisher.class.getName());

    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange contributionEventsExchange;

    public ContributionEventPublisher(RabbitTemplate rabbitTemplate, TopicExchange contributionEventsExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.contributionEventsExchange = contributionEventsExchange;
    }

    public void publishContributionAdded(ContributionEventDTO event) {
        log.info("Publishing ContributionAddedEvent for fundraisingId=" + event.getFundraisingId());
        rabbitTemplate.convertAndSend(contributionEventsExchange.getName(),
                EventFundraisingsRoutingKeys.CONTRIBUTION_ADDED,
                event);
        log.info("Published ContributionAddedEvent for fundraisingId={}");
    }

}
