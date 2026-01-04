package it.unisalento.music_virus_project.billing_service.domain.entity.fee;

import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeePeriod;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;

import java.math.BigDecimal;
import java.util.List;

public class Subscription extends FeePlan{
    private List<Role> isApplicatedTo;
    private FeePeriod feePeriod;
    private BigDecimal amount;

    public Subscription() {
        super();
        this.setFeeType(FeeType.SUBSCRIPTION);
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
}
