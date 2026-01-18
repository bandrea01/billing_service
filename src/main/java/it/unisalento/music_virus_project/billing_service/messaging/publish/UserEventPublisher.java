//package it.unisalento.music_virus_project.billing_service.messaging;

//@Component
//public class UserEventPublisher {
//
//    private static final Logger log = LoggerFactory.getLogger(UserEventPublisher.class);
//
//    private final RabbitTemplate rabbitTemplate;
//    private final TopicExchange userEventsExchange;
//
//    public UserEventPublisher(RabbitTemplate rabbitTemplate, TopicExchange userEventsExchange) {
//        this.rabbitTemplate = rabbitTemplate;
//        this.userEventsExchange = userEventsExchange;
//    }
//
//    public void publishUserRegistered(UserCreatedEventDTO event) {
//        rabbitTemplate.convertAndSend(userEventsExchange.getName(),
//                UserEventRoutingKeys.USER_CREATED,
//                event);
//        log.info("Published UserRegisteredEvent for userId={}", event.getUserId());
//    }
//
//}
