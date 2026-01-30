package it.unisalento.music_virus_project.billing_service.dto.fee;

import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;

import java.math.BigDecimal;
import java.time.Instant;

public class TaxResponseDTO {
    private FeeType feeType;
    private String feePlanId;
    private String taxName;
    private BigDecimal percentageOnTotal;
    private Instant activeSince;

    public TaxResponseDTO(){}

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
    public String getTaxName() {
        return taxName;
    }
    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }
    public BigDecimal getPercentageOnTotal() {
        return percentageOnTotal;
    }
    public void setPercentageOnTotal(BigDecimal percentageOnTotal) {
        this.percentageOnTotal = percentageOnTotal;
    }
    public Instant getActiveSince() {
        return activeSince;
    }
    public void setActiveSince(Instant activeSince) {
        this.activeSince = activeSince;
    }
}
