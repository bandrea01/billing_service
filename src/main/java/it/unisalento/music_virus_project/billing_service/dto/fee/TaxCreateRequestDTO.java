package it.unisalento.music_virus_project.billing_service.dto.fee;

import it.unisalento.music_virus_project.billing_service.domain.enums.TaxEnum;

import java.time.Instant;

public class TaxCreateRequestDTO {
    private TaxEnum taxName;
    private double percentageOnTotal;
    private Instant activeSince;

    public TaxCreateRequestDTO() {}

    public TaxEnum getTaxName() {
        return taxName;
    }
    public void setTaxName(TaxEnum taxName) {
        this.taxName = taxName;
    }
    public double getPercentageOnTotal() {
        return percentageOnTotal;
    }
    public void setPercentageOnTotal(double percentageOnTotal) {
        this.percentageOnTotal = percentageOnTotal;
    }
    public Instant getActiveSince() {return activeSince;}
    public void setActiveSince(Instant activeSince) {this.activeSince = activeSince;}
}
