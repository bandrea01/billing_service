package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionCreateRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.contribution.TopContributorsListResponseDTO;
import it.unisalento.music_virus_project.billing_service.service.IContributionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/contribution")
public class ContributionController {

    private final IContributionService contributionService;

    public ContributionController(IContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @GetMapping("/{fundraisingId}/top-contributors")
    public ResponseEntity<TopContributorsListResponseDTO> getTopContributionsByFundraisingId(@PathVariable String fundraisingId) {
        TopContributorsListResponseDTO contributions = contributionService.getTopContributionsByFundraisingId(fundraisingId);
        return ResponseEntity.ok(contributions);
    }

    @PostMapping
    public ResponseEntity<ContributionResponseDTO> createContribution(
            @AuthenticationPrincipal Jwt principal,
            @RequestBody ContributionCreateRequestDTO createContributionRequest
    ) {
        String userId = principal.getClaimAsString("userId");

        ContributionResponseDTO contribution = contributionService.createContribution(
                createContributionRequest.getFundraisingId(),
                userId,
                createContributionRequest.getArtistId(),
                createContributionRequest.getAmount(),
                createContributionRequest.getContributionVisibility()
        );

        return ResponseEntity.ok(contribution);
    }
}
