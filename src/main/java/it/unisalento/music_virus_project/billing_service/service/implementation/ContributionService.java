package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Contribution;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionStatus;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionVisibility;
import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.contribution.TopContributorResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.contribution.TopContributorsListResponseDTO;
import it.unisalento.music_virus_project.billing_service.messaging.dto.ContributionEventDTO;
import it.unisalento.music_virus_project.billing_service.messaging.publish.ContributionEventPublisher;
import it.unisalento.music_virus_project.billing_service.repositories.IContributionRepository;
import it.unisalento.music_virus_project.billing_service.service.IContributionService;
import it.unisalento.music_virus_project.billing_service.service.ITransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ContributionService implements IContributionService {

    private final IContributionRepository contributionRepository;
    private final ITransactionService transactionService;

    private final ContributionEventPublisher contributionEventPublisher;
    private final AccountBalanceService accountBalanceService;

    public ContributionService(
            IContributionRepository contributionRepository,
            ITransactionService transactionService,
            ContributionEventPublisher contributionEventPublisher,
            AccountBalanceService accountBalanceService) {
        this.contributionRepository = contributionRepository;
        this.transactionService = transactionService;
        this.contributionEventPublisher = contributionEventPublisher;
        this.accountBalanceService = accountBalanceService;
    }

    @Override
    public TopContributorsListResponseDTO getTopContributionsByFundraisingId(String fundraisingId) {
        TopContributorsListResponseDTO response = new TopContributorsListResponseDTO();
        response.setFundraisingId(fundraisingId);
        contributionRepository.findTop3Contributors(fundraisingId)
                .forEach(r -> {
                    TopContributorResponseDTO dto = new TopContributorResponseDTO();
                    dto.setUserId(r.getUserId());
                    dto.setAnonymous(r.getAllAnonymous() != null && r.getAllAnonymous() == 1);
                    response.addTopContributor(dto);
                });
        return response;
    }

    @Override
    public ContributionResponseDTO createContribution(String fundraisingId, String fanUserId, String artistId, BigDecimal amount, ContributionVisibility visibility) {

        Contribution contribution = new Contribution();
        contribution.setFundraisingId(fundraisingId);
        contribution.setUserId(fanUserId);
        contribution.setArtistId(artistId);
        contribution.setAmount(amount);
        contribution.setStatus(ContributionStatus.CAPTURED);
        contribution.setVisibility(visibility);

        try {
            contribution = contributionRepository.save(contribution);

            transactionService.recordContributionPayment(
                    fanUserId,
                    artistId,
                    amount,
                    contribution.getContributionId()
            );

            ContributionEventDTO event = new ContributionEventDTO();
            event.setFundraisingId(contribution.getFundraisingId());
            event.setAmount(contribution.getAmount());

            contributionEventPublisher.publishContributionAdded(event);

            return mapToDTO(contribution);
        } catch (Exception e) {
            // Rollback in case of failure
            try {
                accountBalanceService.creditByUserId(fanUserId, amount);
            } catch (Exception ignored) {}
            if (contribution.getContributionId() != null) {
                try {
                    contributionRepository.deleteById(contribution.getContributionId());
                } catch (Exception ignored) {
                }
            }
            throw e;
        }

    }

    private ContributionResponseDTO mapToDTO(Contribution contribution) {
        ContributionResponseDTO dto = new ContributionResponseDTO();
        dto.setContributionId(contribution.getContributionId());
        dto.setFundraisingId(contribution.getFundraisingId());
        dto.setFanId(contribution.getUserId());
        dto.setAmount(contribution.getAmount());
        dto.setContributionStatus(contribution.getStatus());
        dto.setCreatedAt(contribution.getCreatedAt());
        return dto;
    }
}
