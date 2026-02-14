package it.unisalento.music_virus_project.billing_service.messaging.listener;

import it.unisalento.music_virus_project.billing_service.domain.entity.Contribution;
import it.unisalento.music_virus_project.billing_service.domain.entity.Refund;
import it.unisalento.music_virus_project.billing_service.messaging.dto.EventCreationDTO;
import it.unisalento.music_virus_project.billing_service.messaging.dto.FundraisingRefundDTO;
import it.unisalento.music_virus_project.billing_service.repositories.IContributionRepository;
import it.unisalento.music_virus_project.billing_service.repositories.IRefundRepository;
import it.unisalento.music_virus_project.billing_service.service.ITransactionService;
import it.unisalento.music_virus_project.billing_service.service.implementation.AccountService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventFundraisingListener {

    private final IContributionRepository contributionRepository;
    private final IRefundRepository refundRepository;
    private final AccountService accountService;
    private final ITransactionService transactionService;

    public EventFundraisingListener(
            IContributionRepository contributionRepository,
            IRefundRepository refundRepository,
            AccountService accountService,
            ITransactionService transactionService
    ) {
        this.contributionRepository = contributionRepository;
        this.refundRepository = refundRepository;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @RabbitListener(queues = "${app.rabbitmq.contribution-events-queue}")
    public void handleRefund(FundraisingRefundDTO event) {
        System.out.println("Ricevuto evento di rimborso per fundraisingId: " + event.getFundraisingId() + " e artistId: " + event.getArtistId());
        List<Contribution> contributions = contributionRepository.findAllByFundraisingId(event.getFundraisingId());
        if (contributions.isEmpty()) return;

        for (Contribution contribution : contributions) {

            // Continue if already refunded
            if (refundRepository.findByContributionId(contribution.getContributionId()).isPresent()) {
                continue;
            }

            Refund refund = new Refund();
            refund.setFundraisingId(event.getFundraisingId());
            refund.setContributionId(contribution.getContributionId());
            refund.setUserId(contribution.getUserId());
            refund.setArtistId(event.getArtistId());
            refund.setAmount(contribution.getAmount());

            refund = refundRepository.save(refund);

            if (refund.getRefundId() == null) {
                throw new RuntimeException("Errore durante la registrazione del rimborso");
            }

            // Transaction creation
            try {
                transactionService.recordRefund(
                        event.getArtistId(),
                        contribution.getUserId(),
                        contribution.getAmount(),
                        refund.getRefundId()
                );
            } catch (Exception e) {
                refundRepository.deleteById(refund.getRefundId());
                throw new RuntimeException("Errore durante la registrazione della transazione di rimborso: " + e.getMessage());
            }

        }
    }

    @RabbitListener(queues = "${app.rabbitmq.event-creation-queue}")
    public void handleEventCreation(EventCreationDTO event) {
        System.out.println("Ricevuto evento di creazione, pagamento per artist con id: " + event.getArtistId() + " e amount: " + event.getAmount());
        try {
            transactionService.recordEventPayment(
                    event.getEventId(),
                    event.getArtistId(),
                    event.getAmount()
            );
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il trasferimento dei fondi all'artista: " + e.getMessage());
        }
    }
}
