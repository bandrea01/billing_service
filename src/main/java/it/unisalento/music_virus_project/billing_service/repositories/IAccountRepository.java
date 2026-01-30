package it.unisalento.music_virus_project.billing_service.repositories;

import it.unisalento.music_virus_project.billing_service.domain.entity.Account;
import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IAccountRepository extends MongoRepository<Account, String> {
    Optional<Account> findByAccountId(String accountId);
    Optional<Account> findByUserId(String userId);
    Optional<Account> findFirstByRole(Role role);
    List<Account> findByRole(Role role);
    List<Account> findByUserIdIn(Collection<String> userIds);
}
