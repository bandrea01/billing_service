package it.unisalento.music_virus_project.billing_service.dto.ticket;

import it.unisalento.music_virus_project.billing_service.domain.enums.TicketStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class TicketResponseDTO {
    private String ticketId;
    private String eventId;
    private String ticketCode;
    private BigDecimal contributionAmount;
    private TicketStatus status;
    private Instant createdAt;

    public TicketResponseDTO() {}

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

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

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
