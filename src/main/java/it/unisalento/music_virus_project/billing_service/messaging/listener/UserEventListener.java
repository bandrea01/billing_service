package it.unisalento.music_virus_project.billing_service.messaging.listener;

import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.messaging.dto.UserApprovalChangedEventDTO;
import it.unisalento.music_virus_project.billing_service.messaging.dto.UserCreatedEventDTO;
import it.unisalento.music_virus_project.billing_service.messaging.dto.UserEnabledChangedEventDTO;
import it.unisalento.music_virus_project.billing_service.service.implementation.AccountService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserEventListener {

    private static final Logger log = LoggerFactory.getLogger(UserEventListener.class);

    private final AccountService accountService;

    public UserEventListener(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Handles the UserCreatedEvent by creating a bank account for the new user.
     */
    @RabbitListener(queues = "${app.rabbitmq.user-creation-queue}")
    public void createBankAccountOnUserCreation(UserCreatedEventDTO event) {
        log.info("Received UserCreatedEvent with userId={}", event.getUserId());
        AccountResponseDTO account = accountService.createAccount(event.getUserId(), event.getRole());
        if (account == null) {
            log.error("Failed to create account for userId={}", event.getUserId());
            return;
        }
        log.info("Account created with id={} for userId={}", account.getAccountId(), event.getUserId());
    }

    /**
     * Handles the UserApprovalChangedEvent by enabling or suspending the user's account status.
     */
    @RabbitListener(queues = "${app.rabbitmq.user-approval-queue}")
    public void handleUserApprovalChangedEvent(UserApprovalChangedEventDTO event) {
        log.info("Received UserApprovalChangedEvent with userId={}, approved={}", event.getUserId(), event.isApproved());
        if(event.isApproved()) {
            accountService.enableAccountByUserId(event.getUserId());
            log.info("Account enabled for userId={}", event.getUserId());
        } else {
            accountService.suspendAccountByUserId(event.getUserId());
            log.info("Account suspanded for userId={}", event.getUserId());
        }
    }

    /**
     * Handles the UserEnableChangedEvent by enabling or closing the user's account.
     */
    @RabbitListener(queues = "${app.rabbitmq.user-enable-queue}")
    public void handleUserEnableChangedEvent(UserEnabledChangedEventDTO event) {
        log.info("Received UserEnableChangedEvent with userId={}, enabled={}", event.getUserId(), event.isEnabled());
        if (!event.isEnabled()) {
            accountService.enableAccountByUserId(event.getUserId());
            log.info("Account enabled for userId={}", event.getUserId());
        } else {
            accountService.closeAccountByUserId(event.getUserId());
            log.info("Account suspanded for userId={}", event.getUserId());
        }
    }

}
