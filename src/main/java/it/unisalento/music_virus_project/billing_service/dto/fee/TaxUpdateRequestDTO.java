package it.unisalento.music_virus_project.billing_service.dto.fee;

import it.unisalento.music_virus_project.billing_service.domain.enums.TaxEnum;

import java.time.Instant;

public class TaxUpdateRequestDTO {
    private TaxEnum taxName;
    private Double eventTaxPercentage;
    private Instant activeSince;

    public TaxUpdateRequestDTO() {}

    public TaxEnum getTaxName() {
        return taxName;
    }
    public void setTaxName(TaxEnum taxName) {
        this.taxName = taxName;
    }
    public Double getEventTaxPercentage() {
        return eventTaxPercentage;
    }
    public void setEventTaxPercentage(Double eventTaxPercentage) {
        this.eventTaxPercentage = eventTaxPercentage;
    }
    public Instant getActiveSince() {return activeSince;}
    public void setActiveSince(Instant activeSince) {this.activeSince = activeSince;}
}
