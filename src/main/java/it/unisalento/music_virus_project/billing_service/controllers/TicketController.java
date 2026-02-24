package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.dto.ticket.TicketCreationRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.ticket.TicketListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.ticket.TicketResponseDTO;
import it.unisalento.music_virus_project.billing_service.service.ITicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/tickets")
public class TicketController {
    private final ITicketService ticketService;

    public TicketController(ITicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<TicketListResponseDTO> getAllTicketsByUserId(@AuthenticationPrincipal Jwt principal) {
        String userId = principal.getClaimAsString("userId");
        var response = ticketService.getAllTicketsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable String ticketId) {
        var response = ticketService.getTicketById(ticketId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(@AuthenticationPrincipal Jwt principal, @RequestBody TicketCreationRequestDTO request) {
        String userId = principal.getClaimAsString("userId");
        var response = ticketService.createTicket(userId, request.getEventId(), request.getFundraisingId(), request.getContributionAmount());
        return ResponseEntity.ok(response);
    }
}
