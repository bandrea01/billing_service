package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Contribution;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionStatus;
import it.unisalento.music_virus_project.billing_service.repositories.IContributionRepository;
import it.unisalento.music_virus_project.billing_service.service.IContributionService;
import it.unisalento.music_virus_project.billing_service.service.ITransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ContributionService implements IContributionService {

    private final IContributionRepository contributionRepository;
    private final AccountService accountService;
    private final ITransactionService transactionService;

    public ContributionService(
            IContributionRepository contributionRepository,
            AccountService accountService,
            ITransactionService transactionService
    ) {
        this.contributionRepository = contributionRepository;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Override
    public Contribution createContribution(String fundraisingId, String fanUserId, String artistId, BigDecimal amount) {

        accountService.debit(fanUserId, amount);

        Contribution saved = null;
        try {
            Contribution c = new Contribution();
            c.setFundraisingId(fundraisingId);
            c.setUserId(fanUserId);
            c.setArtistId(artistId);
            c.setAmount(amount);
            c.setStatus(ContributionStatus.CAPTURED);

            saved = contributionRepository.save(c);

            transactionService.recordContributionPayment(
                    fanUserId,
                    artistId,
                    amount,
                    saved.getContributionId()
            );

            return saved;
        } catch (Exception e) {
            try { accountService.credit(fanUserId, amount); } catch (Exception ignored) {}
            if (saved != null && saved.getContributionId() != null) {
                try { contributionRepository.deleteById(saved.getContributionId()); } catch (Exception ignored) {}
            }
            throw e;
        }
    }
}
