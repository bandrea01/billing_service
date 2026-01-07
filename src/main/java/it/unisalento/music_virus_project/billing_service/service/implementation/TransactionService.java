package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Transaction;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionReferenceType;
import it.unisalento.music_virus_project.billing_service.exceptions.NotFoundException;
import it.unisalento.music_virus_project.billing_service.repositories.ITransactionRepository;
import it.unisalento.music_virus_project.billing_service.service.ITransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService implements ITransactionService {
    private final ITransactionRepository transactionRepository;

    public TransactionService(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository.findTransactionByTransactionId(transactionId);
        if (transaction == null) {
            throw new NotFoundException("Errore: transazione non trovata con ID " + transactionId);
        }
                        return transaction;
    }

    public List<Transaction> getTransactionsBySenderId(String senderId) {
        return transactionRepository.findAllBySenderId(senderId);
    }

    public List<Transaction> getTransactionsByReceiverId(String receiverId) {
        return transactionRepository.findAllByReceiverId(receiverId);
    }

    public List<Transaction> getTransactionsByReferenceId(String referenceId) {
        return transactionRepository.findAllByReferenceId(referenceId);
    }

    public List<Transaction> getTransactionsByReferenceType(TransactionReferenceType referenceType) {
        return transactionRepository.findAllByReferenceType(referenceType);
    }

    public Transaction recordFeePayment(String senderId, String receiverId, BigDecimal amount, String feePlanId, FeeType feeType) {
        Transaction transaction = new Transaction(feePlanId, feeType.name(), senderId, receiverId, amount);
        transaction.validate();
        return transactionRepository.save(transaction);
    }

    public Transaction recordContributionPayment(String senderId, String receiverId, BigDecimal amount, String contributionId) {
        Transaction transaction = Transaction.forContribution(contributionId, senderId, receiverId, amount);
        transaction.validate();
        return transactionRepository.save(transaction);
    }

    public Transaction recordRefund(String senderId, String receiverId, BigDecimal amount, String refundId) {
        Transaction transaction = Transaction.forRefund(refundId, senderId, receiverId, amount);
        transaction.validate();
        return transactionRepository.save(transaction);
    }

}
