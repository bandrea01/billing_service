package it.unisalento.music_virus_project.billing_service.dto.transaction;

import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionResponseDTO {
    private String transactionId;
    private TransactionType transactionType;
    private String senderId;
    private String receiverId;
    private BigDecimal amount;
    private Instant createdAt;

    public TransactionResponseDTO() {}

    public TransactionResponseDTO(String transactionId, TransactionType transactionType, String senderId, String receiverId, BigDecimal amount, Instant createdAt) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public TransactionType getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    public String getSenderId() {
        return senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public String getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
