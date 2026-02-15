package it.unisalento.music_virus_project.billing_service.dto.ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketListResponseDTO {
    private List<TicketResponseDTO> tickets;

    public TicketListResponseDTO() {
        this.tickets = new ArrayList<>();
    }

    public List<TicketResponseDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketResponseDTO> tickets) {
        this.tickets = tickets;
    }

    public void addTicket(TicketResponseDTO ticket) {
        this.tickets.add(ticket);
    }

}
