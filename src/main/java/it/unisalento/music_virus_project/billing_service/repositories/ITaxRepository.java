package it.unisalento.music_virus_project.billing_service.repositories;

import it.unisalento.music_virus_project.billing_service.domain.entity.fee.Tax;
import it.unisalento.music_virus_project.billing_service.domain.enums.TaxEnum;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ITaxRepository extends MongoRepository<Tax, String> {
    Tax findTaxByFeePlanId(String feePlanId);
    Tax findTaxByTaxName(TaxEnum taxName);
}
