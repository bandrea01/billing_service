package it.unisalento.music_virus_project.billing_service.repositories;

import it.unisalento.music_virus_project.billing_service.domain.entity.Refund;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IRefundRepository extends MongoRepository<Refund, String> {
    List<Refund> findByFundraisingId(String fundraisingId);
    List<Refund> findByUserId(String userId);
    Optional<Refund> findByContributionId(String contributionId);
}
