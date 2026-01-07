package it.unisalento.music_virus_project.billing_service.repositories;

import it.unisalento.music_virus_project.billing_service.domain.entity.Transaction;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionReferenceType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ITransactionRepository extends MongoRepository<Transaction, String> {
    Transaction findTransactionByTransactionId(String transactionId);
    List<Transaction> findAllBySenderId(String senderId);
    List<Transaction> findAllByReceiverId(String receiverId);
    List<Transaction> findAllByReferenceType (TransactionReferenceType referenceType);
    List<Transaction> findAllByReferenceId (String referenceId);
}
