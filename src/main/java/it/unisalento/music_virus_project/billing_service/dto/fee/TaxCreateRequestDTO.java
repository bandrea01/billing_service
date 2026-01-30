package it.unisalento.music_virus_project.billing_service.dto.fee;

import it.unisalento.music_virus_project.billing_service.domain.enums.TaxEnum;

import java.math.BigDecimal;
import java.time.Instant;

public class TaxCreateRequestDTO {
    private TaxEnum taxName;
    private BigDecimal percentageOnTotal;
    private Instant activeSince;

    public TaxCreateRequestDTO() {}

    public TaxEnum getTaxName() {
        return taxName;
    }
    public void setTaxName(TaxEnum taxName) {
        this.taxName = taxName;
    }
    public BigDecimal getPercentageOnTotal() {
        return percentageOnTotal;
    }
    public void setPercentageOnTotal(BigDecimal percentageOnTotal) {
        this.percentageOnTotal = percentageOnTotal;
    }
    public Instant getActiveSince() {return activeSince;}
    public void setActiveSince(Instant activeSince) {this.activeSince = activeSince;}
}
