package it.unisalento.music_virus_project.billing_service.repositories;


import it.unisalento.music_virus_project.billing_service.domain.entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ITicketRepository extends MongoRepository<Ticket, String> {
    Ticket findTicketByTicketId(String ticketId);
    List<Ticket> findAllByUserId(String userId);
}
