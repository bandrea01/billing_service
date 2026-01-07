package it.unisalento.music_virus_project.billing_service.repositories;

import it.unisalento.music_virus_project.billing_service.domain.entity.Contribution;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionStatus;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionVisibility;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IContributionRepository extends MongoRepository<Contribution, String> {

    Contribution findByContributionId(String contributionId);
    List<Contribution> findAllByContributionVisibility(ContributionVisibility contributionVisibility);
    List<Contribution> findAllByStatus(ContributionStatus status);
    List<Contribution> findAllByFundraisingId(String fundraisingId);
    List<Contribution> findAllByFundraisingIdAndContributionVisibility(String fundraisingId, ContributionVisibility contributionVisibility);
    List<Contribution> findAllByFundraisingIdAndStatus(String fundraisingId, ContributionStatus status);
    List<Contribution> findAllByUserId(String userId);
    List<Contribution> findAllByUserIdAndContributionVisibility(String userId, ContributionVisibility contributionVisibility);
    List<Contribution> findAllByUserIdAndStatus(String userId, ContributionStatus status);
}
