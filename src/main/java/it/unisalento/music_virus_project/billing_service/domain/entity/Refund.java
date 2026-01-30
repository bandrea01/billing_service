package it.unisalento.music_virus_project.billing_service.domain.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "refunds")
public class Refund {

    @Id
    private String refundId;

    @Indexed
    private String fundraisingId;

    @Indexed(unique = true)
    private String contributionId;

    @Indexed
    private String userId;

    @Indexed
    private String artistId;

    private BigDecimal amount;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastUpdatedAt;

    public Refund() {}

    public String getRefundId() { return refundId; }
    public void setRefundId(String refundId) { this.refundId = refundId; }

    public String getFundraisingId() { return fundraisingId; }
    public void setFundraisingId(String fundraisingId) { this.fundraisingId = fundraisingId; }

    public String getContributionId() { return contributionId; }
    public void setContributionId(String contributionId) { this.contributionId = contributionId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getArtistId() { return artistId; }
    public void setArtistId(String artistId) { this.artistId = artistId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastUpdatedAt() { return lastUpdatedAt; }
}
