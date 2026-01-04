package it.unisalento.music_virus_project.billing_service.repositories;

import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.entity.fee.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ISubscriptionRepository extends MongoRepository<Subscription, String> {

    Subscription findSubscriptionByFeePlanId(String feePlanId);
    List<Subscription> findSubscriptionByIsApplicatedToContains(Role isApplicatedTo);

}