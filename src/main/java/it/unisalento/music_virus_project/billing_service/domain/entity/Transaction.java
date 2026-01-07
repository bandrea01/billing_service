package it.unisalento.music_virus_project.billing_service.domain.entity;

import it.unisalento.music_virus_project.billing_service.domain.entity.fee.FeePlan;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionReferenceType;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Transaction {
    @Id
    private String transactionId;

    @Indexed
    private TransactionType transactionType; // FEE_PAYMENT, CONTRIBUTION_PAYMENT, REFUND
    @Indexed
    private String referenceId; // feePlanId, contributionId, refundId
    @Indexed
    private TransactionReferenceType referenceType; // FEE_PLAN, CONTRIBUTION, REFUND
    @Indexed
    private String senderId;
    @Indexed
    private String receiverId;

    private FeeType feeType;
    private BigDecimal amount;

    @CreatedDate
    private Instant createdAt;

    public Transaction() {
        this.createdAt = Instant.now();
    }

    public Transaction(String feePlanId, String feeType, String senderId, String receiverId, BigDecimal amount) {
        this.transactionType = TransactionType.FEE_PAYMENT;
        this.referenceType = TransactionReferenceType.FEE_PLAN;
        this.referenceId = feePlanId;
        this.feeType = FeeType.valueOf(feeType);
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.createdAt = Instant.now();
    }

    private Transaction(TransactionType transactionType,
                        TransactionReferenceType referenceType,
                        String referenceId,
                        String senderId,
                        String receiverId,
                        BigDecimal amount) {
        this.transactionType = transactionType;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.createdAt = Instant.now();
    }

    public static Transaction forContribution(String contributionId, String senderId, String receiverId, BigDecimal amount) {
        return new Transaction(TransactionType.CONTRIBUTION_PAYMENT,
                TransactionReferenceType.CONTRIBUTION,
                contributionId,
                senderId,
                receiverId,
                amount);
    }

    public static Transaction forRefund(String refundId, String senderId, String receiverId, BigDecimal amount) {
        return new Transaction(TransactionType.REFUND,
                TransactionReferenceType.REFUND,
                refundId,
                senderId,
                receiverId,
                amount);
    }

    public void validate() {
        Objects.requireNonNull(transactionType, "È necessario specificare il tipo di transazione");
        Objects.requireNonNull(senderId, "È necessario specificare l'ID del mittente");
        Objects.requireNonNull(receiverId, "È necessario specificare l'ID del destinatario");
        Objects.requireNonNull(amount, "È necessario specificare l'importo della transazione");
        if (amount.signum() <= 0)
            throw new IllegalArgumentException("L'importo della transazione deve essere positivo");

        Objects.requireNonNull(referenceType, "È necessario specificare il tipo di riferimento della transazione");
        Objects.requireNonNull(referenceId, "È necessario specificare l'ID di riferimento della transazione");

        if (transactionType == TransactionType.FEE_PAYMENT && referenceType != TransactionReferenceType.FEE_PLAN)
            throw new IllegalArgumentException("FEE_PAYMENT dovrebbe fare riferimento a FEE_PLAN");

        if (transactionType == TransactionType.CONTRIBUTION_PAYMENT && referenceType != TransactionReferenceType.CONTRIBUTION)
            throw new IllegalArgumentException("CONTRIBUTION_PAYMENT dovrebbe fare riferimento a CONTRIBUTION");

        if (transactionType == TransactionType.REFUND && referenceType != TransactionReferenceType.REFUND)
            throw new IllegalArgumentException("REFUND dovrebbe fare riferimento a REFUND");
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

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public TransactionReferenceType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(TransactionReferenceType referenceType) {
        this.referenceType = referenceType;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
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
