package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.domain.entity.Contribution;
import it.unisalento.music_virus_project.billing_service.service.IContributionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/billing/contribution")
public class ContributionController {

    private final IContributionService contributionService;

    public ContributionController(IContributionService contributionService) {
        this.contributionService = contributionService;
    }

    public static class CreateContributionRequest {
        private String fundraisingId;
        private String artistId;
        private BigDecimal amount;

        public String getFundraisingId() { return fundraisingId; }
        public void setFundraisingId(String fundraisingId) { this.fundraisingId = fundraisingId; }

        public String getArtistId() { return artistId; }
        public void setArtistId(String artistId) { this.artistId = artistId; }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }

    public static class ContributionResponse {
        private String contributionId;
        private String fundraisingId;
        private String userId;
        private String artistId;
        private BigDecimal amount;

        public static ContributionResponse from(Contribution c) {
            ContributionResponse r = new ContributionResponse();
            r.contributionId = c.getContributionId();
            r.fundraisingId = c.getFundraisingId();
            r.userId = c.getUserId();
            r.artistId = c.getArtistId();
            r.amount = c.getAmount();
            return r;
        }

        public String getContributionId() { return contributionId; }
        public String getFundraisingId() { return fundraisingId; }
        public String getUserId() { return userId; }
        public String getArtistId() { return artistId; }
        public BigDecimal getAmount() { return amount; }
    }

    @PostMapping
    public ResponseEntity<ContributionResponse> createContribution(
            @AuthenticationPrincipal Jwt principal,
            @RequestBody CreateContributionRequest req
    ) {
        String fanUserId = principal.getClaimAsString("userId");

        Contribution created = contributionService.createContribution(
                req.getFundraisingId(),
                fanUserId,
                req.getArtistId(),
                req.getAmount()
        );

        return ResponseEntity.ok(ContributionResponse.from(created));
    }
}
