package it.unisalento.music_virus_project.billing_service.repositories;

import it.unisalento.music_virus_project.billing_service.domain.entity.Contribution;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IContributionRepository extends MongoRepository<Contribution, String> {
    List<Contribution> findAllByFundraisingId(String fundraisingId);
}
