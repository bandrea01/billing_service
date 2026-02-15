package it.unisalento.music_virus_project.billing_service.service;

import it.unisalento.music_virus_project.billing_service.dto.ticket.TicketListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.ticket.TicketResponseDTO;

import java.math.BigDecimal;

public interface ITicketService {
    TicketListResponseDTO getAllTicketsByUserId(String userId);
    TicketResponseDTO getTicketById(String ticketId);
    TicketResponseDTO createTicket(String userId, String eventId, BigDecimal contributionAmount);
}
