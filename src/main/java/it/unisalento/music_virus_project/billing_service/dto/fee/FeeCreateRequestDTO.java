package it.unisalento.music_virus_project.billing_service.dto.fee;

import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeePeriod;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class FeeCreateRequestDTO {
    private FeeType feeType;
    private List<Role> isApplicatedTo;
    private FeePeriod feePeriod;
    private BigDecimal amount;
    private Instant activeSince;

    public FeeCreateRequestDTO() {}

    public FeeType getFeeType() {
        return feeType;
    }
    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
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
    public Instant getActiveSince() {return activeSince;}
    public void setActiveSince(Instant activeSince) {this.activeSince = activeSince;}
}
