package it.unisalento.music_virus_project.billing_service.dto.contribution;

import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionVisibility;

import java.math.BigDecimal;

public class ContributionCreateRequestDTO {

    private String fundraisingId;
    private String artistId;
    private String userId;
    private BigDecimal amount;
    private ContributionVisibility contributionVisibility;

    public ContributionCreateRequestDTO() {}

    public String getFundraisingId() {
        return fundraisingId;
    }
    public void setFundraisingId(String fundraisingId) {
        this.fundraisingId = fundraisingId;
    }
    public String getArtistId() {
        return artistId;
    }
    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public ContributionVisibility getContributionVisibility() {
        return contributionVisibility;
    }
    public void setContributionVisibility(ContributionVisibility visibility) {
        this.contributionVisibility = visibility;
    }
}
