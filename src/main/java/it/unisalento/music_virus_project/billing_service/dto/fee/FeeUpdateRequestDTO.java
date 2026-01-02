package it.unisalento.music_virus_project.billing_service.dto.fee;

import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeePeriod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class FeeUpdateRequestDTO {
    private List<Role> isApplicatedTo;
    private FeePeriod feePeriod;
    private BigDecimal amount;
    private Instant activeSince;

    public FeeUpdateRequestDTO() {}

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
