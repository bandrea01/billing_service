package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.configuration.JwtProperties;
import it.unisalento.music_virus_project.billing_service.domain.entity.Ticket;
import it.unisalento.music_virus_project.billing_service.domain.enums.TicketStatus;
import it.unisalento.music_virus_project.billing_service.dto.ticket.TicketListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.ticket.TicketResponseDTO;
import it.unisalento.music_virus_project.billing_service.repositories.ITicketRepository;
import it.unisalento.music_virus_project.billing_service.service.ITicketService;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService implements ITicketService {

    private final ITicketRepository ticketRepository;
    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;

    public TicketService(ITicketRepository ticketRepository, JwtProperties jwtProperties, JwtEncoder jwtEncoder) {
        this.jwtProperties = jwtProperties;
        this.jwtEncoder = jwtEncoder;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public TicketListResponseDTO getAllTicketsByUserId(String userId) {
        List<Ticket> tickets = ticketRepository.findAllByUserId(userId);
        return mapToListDTO(tickets);
    }

    @Override
    public TicketResponseDTO getTicketById(String ticketId) {
        Ticket ticket = ticketRepository.findTicketByTicketId(ticketId);
        if (ticket == null) {
            return null;
        }
        return mapToDTO(ticket);
    }

    @Override
    public TicketResponseDTO createTicket(String userId, String eventId, String fundraisingId, BigDecimal contributionAmount) {

        String ticketId = UUID.randomUUID().toString();
        String nonce = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(createdAt)
                .subject(ticketId)
                .claim("eventId", eventId)
                .claim("userId", userId)
                .claim("nonce", nonce)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        String ticketCode = jwtEncoder
                .encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();

        Ticket ticket = new Ticket();
        ticket.setTicketId(ticketId);
        ticket.setUserId(userId);
        ticket.setEventId(eventId);
        ticket.setTicketCode(ticketCode);
        ticket.setNonce(nonce);
        ticket.setContributionAmount(contributionAmount);
        ticket.setCreatedAt(createdAt);
        ticket.setStatus(TicketStatus.ACTIVE);

        ticket = ticketRepository.save(ticket);

        return mapToDTO(ticket);
    }

    //utils
    private TicketResponseDTO mapToDTO(Ticket ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setTicketId(ticket.getTicketId());
        dto.setEventId(ticket.getEventId());
        dto.setContributionAmount(ticket.getContributionAmount());
        dto.setTicketCode(ticket.getTicketCode());
        dto.setStatus(ticket.getStatus());
        dto.setCreatedAt(ticket.getCreatedAt());
        return dto;
    }

    private TicketListResponseDTO mapToListDTO(List<Ticket> tickets) {
        TicketListResponseDTO listDTO = new TicketListResponseDTO();
        for (Ticket ticket : tickets) {
            listDTO.addTicket(mapToDTO(ticket));
        }
        return listDTO;
    }
}
