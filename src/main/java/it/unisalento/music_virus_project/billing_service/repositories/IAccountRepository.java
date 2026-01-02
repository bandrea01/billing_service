package it.unisalento.music_virus_project.billing_service.repositories;

import it.unisalento.music_virus_project.billing_service.domain.entity.Account;
import it.unisalento.music_virus_project.billing_service.domain.enums.AccountStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IAccountRepository extends MongoRepository<Account, String> {

    Account findAccountByAccountId(String accountId);
    Account findAccountByUserId(String userId);
    List<Account> findAccountsByStatus(AccountStatus status);

}
