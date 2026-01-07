package it.unisalento.music_virus_project.billing_service.dto.contribution;

import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionStatus;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionVisibility;

import java.math.BigDecimal;
import java.time.Instant;

public class ContributionResponseDTO {

    private String contributionId;
    private String fundraisingId;
    private String fanId;
    private BigDecimal amount;
    private ContributionVisibility contributionVisibility;
    private ContributionStatus contributionStatus;
    private Instant createdAt;
    private Instant lastUpdate;

    public ContributionResponseDTO() {}

    public String getContributionId() { return contributionId; }
    public void setContributionId(String contributionId) { this.contributionId = contributionId; }
    public String getFundraisingId() { return fundraisingId; }
    public void setFundraisingId(String eventId) { this.fundraisingId = eventId; }
    public String getFanId() { return fanId; }
    public void setFanId(String fanId) { this.fanId = fanId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public ContributionVisibility getContributionVisibility() { return contributionVisibility; }
    public void setContributionVisibility(ContributionVisibility contributionVisibility) { this.contributionVisibility = contributionVisibility; }
    public ContributionStatus getContributionStatus() { return contributionStatus; }
    public void setContributionStatus(ContributionStatus status) { this.contributionStatus = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(Instant updatedAt) { this.lastUpdate = updatedAt; }
}
