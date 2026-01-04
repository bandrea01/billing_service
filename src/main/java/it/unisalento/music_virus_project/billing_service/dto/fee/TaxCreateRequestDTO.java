package it.unisalento.music_virus_project.billing_service.dto.fee;

import it.unisalento.music_virus_project.billing_service.domain.enums.TaxEnum;

import java.time.Instant;

public class TaxCreateRequestDTO {
    private TaxEnum taxName;
    private double eventTaxPercentage;
    private Instant activeSince;

    public TaxCreateRequestDTO() {}

    public TaxEnum getTaxName() {
        return taxName;
    }
    public void setTaxName(TaxEnum taxName) {
        this.taxName = taxName;
    }
    public double getEventTaxPercentage() {
        return eventTaxPercentage;
    }
    public void setEventTaxPercentage(double eventTaxPercentage) {
        this.eventTaxPercentage = eventTaxPercentage;
    }
    public Instant getActiveSince() {return activeSince;}
    public void setActiveSince(Instant activeSince) {this.activeSince = activeSince;}
}
