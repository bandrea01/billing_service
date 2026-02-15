package it.unisalento.music_virus_project.billing_service.dto.ticket;

import java.math.BigDecimal;

public class TicketCreationRequestDTO {
    private String eventId;
    private BigDecimal contributionAmount;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public BigDecimal getContributionAmount() {
        return contributionAmount;
    }

    public void setContributionAmount(BigDecimal contributionAmount) {
        this.contributionAmount = contributionAmount;
    }
}
