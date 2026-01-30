package it.unisalento.music_virus_project.billing_service.domain.entity;

import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionStatus;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionVisibility;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "contributions")
public class Contribution {

    @Id
    private String contributionId;
    @Indexed
    private String fundraisingId;
    @Indexed
    private String userId;
    @Indexed
    private String artistId;
    private BigDecimal amount;
    @Indexed
    private ContributionStatus status = ContributionStatus.CAPTURED;
    private ContributionVisibility visibility = ContributionVisibility.PUBLIC;

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant lastUpdatedAt;

    public Contribution() {}

    public String getContributionId() { return contributionId; }
    public void setContributionId(String contributionId) { this.contributionId = contributionId; }

    public String getFundraisingId() { return fundraisingId; }
    public void setFundraisingId(String fundraisingId) { this.fundraisingId = fundraisingId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getArtistId() { return artistId; }
    public void setArtistId(String artistId) { this.artistId = artistId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public ContributionStatus getStatus() { return status; }
    public void setStatus(ContributionStatus status) { this.status = status; }

    public ContributionVisibility getVisibility() { return visibility; }
    public void setVisibility(ContributionVisibility visibility) { this.visibility = visibility; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastUpdatedAt() { return lastUpdatedAt; }
}
