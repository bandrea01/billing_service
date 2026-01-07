package it.unisalento.music_virus_project.billing_service.domain.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

public class Refund {

    @Id
    private String refundId;

    @Indexed
    private String contributionId;
    @Indexed
    private String userId; //user who received the refund
    @Indexed
    private String accountId; //account where the refund was sent
    @Indexed
    private String fundraisingId;

    @CreatedDate
    private Instant createdAt;
    private Instant lastUpdate;

    public Refund(String contributionId, String userId, String accountId, String fundraisingId) {
        this.contributionId = contributionId;
        this.userId = userId;
        this.accountId = accountId;
        this.fundraisingId = fundraisingId;
        this.createdAt = Instant.now();
        this.lastUpdate = Instant.now();
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
        this.lastUpdate = Instant.now();
    }

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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
        this.lastUpdate = Instant.now();
    }

    public String getFundraisingId() {
        return fundraisingId;
    }

    public void setFundraisingId(String fundraisingId) {
        this.fundraisingId = fundraisingId;
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
