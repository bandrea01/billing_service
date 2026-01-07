package it.unisalento.music_virus_project.billing_service.service;

import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionCreateRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionResponseDTO;

public interface IContributionService {
    ContributionResponseDTO createContribution(ContributionCreateRequestDTO contributionCreateRequestDTO);
    ContributionResponseDTO getContributionById(String contributionId);

    ContributionListResponseDTO getAllContributions();
    ContributionListResponseDTO getAllVisibleContributions();
    ContributionListResponseDTO getAllConfirmedContributions();
    ContributionListResponseDTO getAllRefundedContributions();

    ContributionListResponseDTO getContributionsByFundraisingId(String fundraisingId);
    ContributionListResponseDTO getVisibleContributionsByFundraisingId(String fundraisingId);
    ContributionListResponseDTO getConfirmedContributionsByFundraisingId(String fundraisingId);
    ContributionListResponseDTO getRefundedContributionsByFundraisingId(String fundraisingId);

    ContributionListResponseDTO getContributionsByUserId(String userId);
    ContributionListResponseDTO getVisibleContributionsByUserId(String userId);
    ContributionListResponseDTO getConfirmedContributionsByUserId(String userId);
    ContributionListResponseDTO getRefundedContributionsByUserId(String userId);

    ContributionResponseDTO deleteContribution(String contributionId);
}
