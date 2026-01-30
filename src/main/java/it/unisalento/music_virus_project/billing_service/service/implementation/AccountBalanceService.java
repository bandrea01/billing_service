package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Account;
import it.unisalento.music_virus_project.billing_service.exceptions.InsufficentBalanceException;
import it.unisalento.music_virus_project.billing_service.exceptions.NotFoundException;
import it.unisalento.music_virus_project.billing_service.service.IAccountBalanceService;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountBalanceService implements IAccountBalanceService {

    private final MongoTemplate mongoTemplate;

    public AccountBalanceService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Account debitByUserId(String userId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Errore: l'importo deve essere maggiore di 0");
        }

        Query query = new Query(new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("balance").gte(amount)
        ));

        Update update = new Update().inc("balance", amount.negate());

        Account accountUpdated = mongoTemplate.findAndModify(
                query, update, FindAndModifyOptions.options().returnNew(true),
                Account.class
        );

        if (accountUpdated == null) throw new InsufficentBalanceException("Errore: saldo insufficiente" + userId);
        return accountUpdated;
    }

    @Override
    public Account creditByUserId(String userId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Errore: l'importo deve essere maggiore di 0");
        }

        Query q = new Query(Criteria.where("userId").is(userId));
        Update u = new Update().inc("balance", amount);

        Account updated = mongoTemplate.findAndModify(
                q, u, FindAndModifyOptions.options().returnNew(true),
                Account.class
        );

        if (updated == null) throw new NotFoundException("Errore: account non trovato" + userId);
        return updated;
    }
}
