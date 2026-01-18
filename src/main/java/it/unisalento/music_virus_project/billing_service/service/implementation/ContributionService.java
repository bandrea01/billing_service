package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Account;
import it.unisalento.music_virus_project.billing_service.domain.entity.Contribution;
import it.unisalento.music_virus_project.billing_service.domain.entity.Transaction;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionStatus;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionVisibility;
import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionCreateRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionResponseDTO;
import it.unisalento.music_virus_project.billing_service.messaging.dto.ContributionEventDTO;
import it.unisalento.music_virus_project.billing_service.messaging.publish.ContributionEventPublisher;
import it.unisalento.music_virus_project.billing_service.repositories.IAccountRepository;
import it.unisalento.music_virus_project.billing_service.repositories.IContributionRepository;
import it.unisalento.music_virus_project.billing_service.service.IAccountService;
import it.unisalento.music_virus_project.billing_service.service.IContributionService;
import it.unisalento.music_virus_project.billing_service.service.ITransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContributionService implements IContributionService {

    private final IAccountRepository accountRepository;
    private final IContributionRepository contributionRepository;
    private final ITransactionService transactionService;
    private final ContributionEventPublisher contributionEventPublisher;

    public ContributionService(IAccountRepository accountRepository, ITransactionService transactionService, IContributionRepository contributionRepository, ContributionEventPublisher contributionEventPublisher) {
        this.accountRepository = accountRepository;
        this.contributionRepository = contributionRepository;
        this.transactionService = transactionService;
        this.contributionEventPublisher = contributionEventPublisher;
    }

    @Override
    public ContributionResponseDTO createContribution(ContributionCreateRequestDTO contributionCreateRequestDTO) {

        //check account balance
        Account account = accountRepository.findAccountByUserId(contributionCreateRequestDTO.getUserId());
        if (account == null) {
            throw new RuntimeException("Errore: account non trovato per l'utente con ID " + contributionCreateRequestDTO.getUserId());
        }
        if (account.getBalance().compareTo(contributionCreateRequestDTO.getAmount()) < 0) {
            throw new RuntimeException("Errore: saldo insufficiente! Ricarica il conto per proseguire");
        }

        Contribution contribution = new Contribution(
                contributionCreateRequestDTO.getFundraisingId(),
                contributionCreateRequestDTO.getUserId(),
                contributionCreateRequestDTO.getAmount(),
                contributionCreateRequestDTO.getContributionVisibility()
        );

        // save contribution
        contribution = contributionRepository.save(contribution);
        if (contribution.getContributionId() == null) {
            throw new RuntimeException("Errore durante la creazione del contributo.");
        }

        // deduct amount from user account
        account.setBalance(account.getBalance().subtract(contributionCreateRequestDTO.getAmount()));

        // record transaction
        Transaction transaction = transactionService.recordContributionPayment(
                contributionCreateRequestDTO.getUserId(),
                contributionCreateRequestDTO.getArtistId(),
                contributionCreateRequestDTO.getAmount(),
                contribution.getContributionId()
        );
        if (transaction.getTransactionId() == null) {
            throw new RuntimeException("Errore durante la registrazione della transazione per il contributo.");
        }

        // Rabbitmq event publishing
        contributionEventPublisher.publishContributionAdded(
                new ContributionEventDTO(
                        contribution.getFundraisingId(),
                        contribution.getAmount()
                )
        );

        return mapToDTO(contribution);
    }

    @Override
    public ContributionResponseDTO getContributionById(String contributionId) {
        Contribution contribution = contributionRepository.findByContributionId(contributionId);
        if (contribution == null) {
            throw new RuntimeException("Errore: contributo non trovato con ID " + contributionId);
        }
        return mapToDTO(contribution);
    }

    @Override
    public ContributionListResponseDTO getAllContributions() {
        List<Contribution> contributions = contributionRepository.findAll();
        return mapToListDTO(contributions);
    }

    @Override
    public ContributionListResponseDTO getAllVisibleContributions() {
        List<Contribution> contributions = contributionRepository.findAllByContributionVisibility(ContributionVisibility.PUBLIC);
        return mapToListDTO(contributions);
    }

    @Override
    public ContributionListResponseDTO getAllConfirmedContributions() {
        List<Contribution> contributions = contributionRepository.findAllByStatus(ContributionStatus.CAPTURED);
        return mapToListDTO(contributions);
    }

    @Override
    public ContributionListResponseDTO getAllRefundedContributions() {
        List<Contribution> contributions = contributionRepository.findAllByStatus(ContributionStatus.REFUNDED);
        return mapToListDTO(contributions);
    }

    @Override
    public ContributionListResponseDTO getContributionsByFundraisingId(String fundraisingId) {
        List<Contribution> contributions = contributionRepository.findAllByFundraisingId(fundraisingId);
        return mapToListDTO(contributions);
    }

    @Override
    public ContributionListResponseDTO getVisibleContributionsByFundraisingId(String fundraisingId) {
        List<Contribution> contributions = contributionRepository.findAllByFundraisingIdAndContributionVisibility(fundraisingId, ContributionVisibility.PUBLIC);
        return mapToListDTO(contributions);
    }

    @Override
    public ContributionListResponseDTO getConfirmedContributionsByFundraisingId(String fundraisingId) {
        List<Contribution> contributions = contributionRepository.findAllByFundraisingIdAndStatus(fundraisingId, ContributionStatus.CAPTURED);
        return mapToListDTO(contributions);
    }

    @Override
    public ContributionListResponseDTO getRefundedContributionsByFundraisingId(String fundraisingId) {
        List<Contribution> contributions = contributionRepository.findAllByFundraisingIdAndStatus(fundraisingId, ContributionStatus.REFUNDED);
        return mapToListDTO(contributions);
    }

    @Override
    public ContributionListResponseDTO getContributionsByUserId(String userId) {
        List<Contribution> contributions = contributionRepository.findAllByUserId(userId);
        return mapToListDTO(contributions);
    }

    @Override
    public ContributionListResponseDTO getVisibleContributionsByUserId(String userId) {
        List<Contribution> contributions = contributionRepository.findAllByUserIdAndContributionVisibility(userId, ContributionVisibility.PUBLIC);
        return mapToListDTO(contributions);
    }

    @Override
    public ContributionListResponseDTO getConfirmedContributionsByUserId(String userId) {
        List<Contribution> contributions = contributionRepository.findAllByUserIdAndStatus(userId, ContributionStatus.CAPTURED);
        return mapToListDTO(contributions);
    }

    @Override
    public ContributionListResponseDTO getRefundedContributionsByUserId(String userId) {
        List<Contribution> contributions = contributionRepository.findAllByUserIdAndStatus(userId, ContributionStatus.REFUNDED);
        return mapToListDTO(contributions);
    }

    @Override
    public ContributionResponseDTO deleteContribution(String contributionId) {
        Contribution contribution = contributionRepository.findByContributionId(contributionId);
        if (contribution == null) {
            throw new RuntimeException("Errore: contributo non trovato con ID " + contributionId);
        }
        contributionRepository.delete(contribution);
        return mapToDTO(contribution);
    }

    //utils
    private ContributionResponseDTO mapToDTO(Contribution contribution) {
        ContributionResponseDTO dto = new ContributionResponseDTO();
        dto.setContributionId(contribution.getContributionId());
        dto.setFundraisingId(contribution.getFundraisingId());
        dto.setFanId(contribution.getUserId());
        dto.setAmount(contribution.getAmount());
        dto.setContributionVisibility(contribution.getContributionVisibility());
        dto.setContributionStatus(contribution.getStatus());
        dto.setCreatedAt(contribution.getCreatedAt());
        dto.setLastUpdate(contribution.getLastUpdate());

        return dto;
    }

    private ContributionListResponseDTO mapToListDTO(java.util.List<Contribution> contributions) {
        ContributionListResponseDTO listDTO = new ContributionListResponseDTO();
        for (Contribution contribution : contributions) {
            listDTO.getContributions().add(mapToDTO(contribution));
        }
        return listDTO;
    }
}
