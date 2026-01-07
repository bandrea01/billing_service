package it.unisalento.music_virus_project.billing_service.domain.entity;

import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionStatus;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionVisibility;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.Instant;

public class Contribution {

    @Id
    private String contributionId;

    @Indexed
    private String userId; // user who made the contribution
    @Indexed
    private String fundraisingId;

    private BigDecimal amount;
    private ContributionVisibility contributionVisibility;
    private ContributionStatus status;

    @CreatedDate
    private Instant createdAt;
    private Instant lastUpdate;

    public Contribution(String fundraisingId, String userId, BigDecimal amount, ContributionVisibility contributionVisibility, ContributionStatus status) {
        this.fundraisingId = fundraisingId;
        this.userId = userId;
        this.amount = amount;
        this.contributionVisibility = contributionVisibility;
        this.status = status;
        this.createdAt = Instant.now();
        this.lastUpdate = Instant.now();
    }
    public Contribution(String fundraisingId, String userId, BigDecimal amount) {
        this.fundraisingId = fundraisingId;
        this.userId = userId;
        this.amount = amount;
        this.contributionVisibility = ContributionVisibility.PUBLIC;
        this.status = ContributionStatus.CAPTURED;
        this.createdAt = Instant.now();
        this.lastUpdate = Instant.now();
    }
    public Contribution(String fundraisingId, String userId, BigDecimal amount, ContributionVisibility contributionVisibility) {
        this.fundraisingId = fundraisingId;
        this.userId = userId;
        this.amount = amount;
        this.contributionVisibility = contributionVisibility;
        this.status = ContributionStatus.CAPTURED;
        this.createdAt = Instant.now();
        this.lastUpdate = Instant.now();
    }
    public Contribution() {}

    public String getContributionId() {
        return contributionId;
    }

    public void setContributionId(String contributionId) {
        this.contributionId = contributionId;
        this.lastUpdate = Instant.now();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        this.lastUpdate = Instant.now();
    }

    public String getFundraisingId() {
        return fundraisingId;
    }

    public void setFundraisingId(String fundraisingId) {
        this.fundraisingId = fundraisingId;
        this.lastUpdate = Instant.now();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        this.lastUpdate = Instant.now();
    }

    public ContributionVisibility getContributionVisibility() {
        return contributionVisibility;
    }

    public void setContributionVisibility(ContributionVisibility contributionVisibility) {
        this.contributionVisibility = contributionVisibility;
        this.lastUpdate = Instant.now();
    }

    public ContributionStatus getStatus() {
        return status;
    }

    public void setStatus(ContributionStatus status) {
        this.status = status;
        this.lastUpdate = Instant.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        this.lastUpdate = Instant.now();
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
