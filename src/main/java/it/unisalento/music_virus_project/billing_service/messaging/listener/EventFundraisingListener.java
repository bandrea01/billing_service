package it.unisalento.music_virus_project.billing_service.messaging.listener;

import it.unisalento.music_virus_project.billing_service.domain.entity.Contribution;
import it.unisalento.music_virus_project.billing_service.domain.entity.Refund;
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

        List<Contribution> contributions = contributionRepository.findByEventId(event.getFundraisingId());
        if (contributions.isEmpty()) return;

        for (Contribution c : contributions) {

            if (refundRepository.findByContributionId(c.getContributionId()).isPresent()) {
                continue;
            }

            Refund r = new Refund();
            r.setFundraisingId(event.getFundraisingId());
            r.setContributionId(c.getContributionId());
            r.setUserId(c.getUserId());
            r.setArtistId(event.getArtistId());
            r.setAmount(c.getAmount());

            Refund saved = refundRepository.save(r);

            accountService.credit(c.getUserId(), c.getAmount());
            transactionService.recordRefund(
                    event.getArtistId(),
                    c.getUserId(),
                    c.getAmount(),
                    saved.getRefundId()
            );
        }
    }
}
