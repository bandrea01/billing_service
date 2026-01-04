package it.unisalento.music_virus_project.billing_service.repositories;

import it.unisalento.music_virus_project.billing_service.domain.entity.FeePlan;
import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IFeeRepository extends MongoRepository<FeePlan, String> {

    FeePlan findFeeByFeePlanId(String feePlanId);
    FeePlan findFeePlanByIsApplicatedToContains(Role isApplicatedTo);
    FeePlan findFeeByFeeType(FeeType feeType);
}