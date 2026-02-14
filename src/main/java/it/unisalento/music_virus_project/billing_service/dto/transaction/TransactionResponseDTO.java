package it.unisalento.music_virus_project.billing_service.dto.transaction;

import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionType;

import java.math.BigDecimal;

public class TransactionResponseDTO {
    private String transactionId;
    private TransactionType transactionType;
    private String senderId;
    private BigDecimal amount;

    public TransactionResponseDTO() {

    }

    public TransactionResponseDTO(String transactionId, TransactionType transactionType, String senderId, BigDecimal amount) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.senderId = senderId;
        this.amount = amount;
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
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
