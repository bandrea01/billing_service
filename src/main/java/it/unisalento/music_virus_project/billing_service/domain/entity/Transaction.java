package it.unisalento.music_virus_project.billing_service.domain.entity;

import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionReferenceType;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "transactions")
public class Transaction {

    @Id
    private String transactionId;

    @Indexed
    private TransactionType transactionType;

    @Indexed
    private TransactionReferenceType referenceType;

    @Indexed
    private String referenceId;

    @Indexed
    private String senderId;

    @Indexed
    private String receiverId;

    private BigDecimal amount;

    @CreatedDate
    private Instant createdAt;

    protected Transaction() {}

    public Transaction(
            TransactionType transactionType,
            TransactionReferenceType referenceType,
            String referenceId,
            String senderId,
            String receiverId,
            BigDecimal amount
    ) {
        this.transactionType = transactionType;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }

    public String getTransactionId() { return transactionId; }
    public TransactionType getTransactionType() { return transactionType; }
    public TransactionReferenceType getReferenceType() { return referenceType; }
    public String getReferenceId() { return referenceId; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public BigDecimal getAmount() { return amount; }
    public Instant getCreatedAt() { return createdAt; }
}
