package it.unisalento.music_virus_project.billing_service.service;

import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionVisibility;
import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.contribution.TopContributorsListResponseDTO;

import java.math.BigDecimal;

public interface IContributionService {
    ContributionResponseDTO createContribution(String fundraisingId, String fanUserId, String artistId, BigDecimal amount, ContributionVisibility visibility);
    TopContributorsListResponseDTO getTopContributionsByFundraisingId(String fundraisingId);
}
