package it.unisalento.music_virus_project.billing_service.service;

import it.unisalento.music_virus_project.billing_service.domain.entity.Transaction;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionReferenceType;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionService {
    Transaction getTransactionById(String transactionId);
    List<Transaction> getTransactionsBySenderId(String senderId);
    List<Transaction> getTransactionsByReceiverId(String receiverId);
    List<Transaction> getTransactionsByReferenceId(String referenceId);
    List<Transaction> getTransactionsByReferenceType(TransactionReferenceType referenceType);
    Transaction recordFeePayment(String senderId, String receiverId, BigDecimal amount, String feePlanId, FeeType feeType);
    Transaction recordContributionPayment(String senderId, String receiverId, BigDecimal amount, String contributionId);
    Transaction recordRefund(String senderId, String receiverId, BigDecimal amount, String refundId);
}
