package it.unisalento.music_virus_project.billing_service.service;

import it.unisalento.music_virus_project.billing_service.domain.entity.Contribution;

import java.math.BigDecimal;

public interface IContributionService {
    Contribution createContribution(String fundraisingId, String fanUserId, String artistId, BigDecimal amount);
}
