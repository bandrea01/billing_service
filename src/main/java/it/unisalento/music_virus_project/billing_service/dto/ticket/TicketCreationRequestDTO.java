package it.unisalento.music_virus_project.billing_service.dto.ticket;

import java.math.BigDecimal;

public class TicketCreationRequestDTO {
    private String eventId;
    private String fundraisingId;
    private BigDecimal contributionAmount;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getFundraisingId() {
        return fundraisingId;
    }

    public void setFundraisingId(String fundraisingId) {
        this.fundraisingId = fundraisingId;
    }

    public BigDecimal getContributionAmount() {
        return contributionAmount;
    }

    public void setContributionAmount(BigDecimal contributionAmount) {
        this.contributionAmount = contributionAmount;
    }
}
