package it.unisalento.music_virus_project.billing_service.dto.fee;

import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeePeriod;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class SubscriptionResponseDTO {
    private FeeType feeType;
    private String feePlanId;
    private List<Role> isApplicatedTo;
    private FeePeriod feePeriod;
    private BigDecimal amount;
    private Instant activeSince;

    public SubscriptionResponseDTO(){}

    public FeeType getFeeType() {
        return feeType;
    }
    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }
    public String getFeePlanId() {
        return feePlanId;
    }
    public void setFeePlanId(String feePlanId) {
        this.feePlanId = feePlanId;
    }
    public List<Role> getIsApplicatedTo() {
        return isApplicatedTo;
    }
    public void setIsApplicatedTo(List<Role> isApplicatedTo) {
        this.isApplicatedTo = isApplicatedTo;
    }
    public FeePeriod getFeePeriod() {
        return feePeriod;
    }
    public void setFeePeriod(FeePeriod feePeriod) {
        this.feePeriod = feePeriod;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Instant getActiveSince() {
        return activeSince;
    }
    public void setActiveSince(Instant activeSince) {
        this.activeSince = activeSince;
    }
}
