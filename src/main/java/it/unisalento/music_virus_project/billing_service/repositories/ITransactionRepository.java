package it.unisalento.music_virus_project.billing_service.repositories;

import it.unisalento.music_virus_project.billing_service.domain.entity.Transaction;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionReferenceType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ITransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findBySenderId(String senderId);

    List<Transaction> findByReceiverId(String receiverId);

    List<Transaction> findByReferenceTypeAndReferenceId(TransactionReferenceType referenceType, String referenceId);

    List<Transaction> findTop10BySenderIdOrReceiverIdOrderByCreatedAt(String senderId, String receiverId);
    List<Transaction> findBySenderIdOrReceiverIdOrderByCreatedAt(String senderId, String receiverId);
    List<Transaction> findTop10ByReceiverIdOrderByCreatedAt(String receiverId);
}
