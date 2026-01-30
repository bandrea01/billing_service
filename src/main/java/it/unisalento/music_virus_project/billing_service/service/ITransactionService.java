package it.unisalento.music_virus_project.billing_service.service;

import it.unisalento.music_virus_project.billing_service.domain.entity.Transaction;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionReferenceType;

import java.math.BigDecimal;

public interface ITransactionService {
    Transaction recordFeePayment(String senderId, String receiverId, BigDecimal amount, String feePlanId, TransactionReferenceType feeReferenceType);
    void recordContributionPayment(String senderId, String receiverId, BigDecimal amount, String contributionId);
    void recordRefund(String senderId, String receiverId, BigDecimal amount, String refundId);
}
