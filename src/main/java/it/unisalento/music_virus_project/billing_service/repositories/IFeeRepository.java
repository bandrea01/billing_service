package it.unisalento.music_virus_project.billing_service.repositories;

import it.unisalento.music_virus_project.billing_service.domain.entity.FeePlan;
import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IFeeRepository extends MongoRepository<FeePlan, String> {

    FeePlan findFeeByFeePlanId(String feePlanId);
    List<FeePlan> findFeePlanByIsApplicatedTo(List<Role> isApplicatedTo);

}