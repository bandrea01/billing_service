package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Transaction;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionReferenceType;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionType;
import it.unisalento.music_virus_project.billing_service.repositories.ITransactionRepository;
import it.unisalento.music_virus_project.billing_service.service.ITransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService implements ITransactionService {

    private final ITransactionRepository transactionRepository;

    public TransactionService(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction recordFeePayment(
            String senderId,
            String receiverId,
            BigDecimal amount,
            String feePlanId,
            TransactionReferenceType feeReferenceType
    ) {
        validate(senderId, receiverId, amount, feePlanId);

        Transaction tx = new Transaction(
                TransactionType.FEE_PAYMENT,
                feeReferenceType,
                feePlanId,
                senderId,
                receiverId,
                amount
        );
        return transactionRepository.save(tx);
    }

    @Override
    public void recordContributionPayment(
            String senderId,
            String receiverId,
            BigDecimal amount,
            String contributionId
    ) {
        validate(senderId, receiverId, amount, contributionId);

        Transaction tx = new Transaction(
                TransactionType.CONTRIBUTION_PAYMENT,
                TransactionReferenceType.CONTRIBUTION,
                contributionId,
                senderId,
                receiverId,
                amount
        );
        transactionRepository.save(tx);
    }

    @Override
    public void recordRefund(
            String senderId,
            String receiverId,
            BigDecimal amount,
            String refundId
    ) {
        validate(senderId, receiverId, amount, refundId);

        Transaction tx = new Transaction(
                TransactionType.REFUND,
                TransactionReferenceType.REFUND,
                refundId,
                senderId,
                receiverId,
                amount
        );
        transactionRepository.save(tx);
    }

    private void validate(String senderId, String receiverId, BigDecimal amount, String referenceId) {
        if (senderId == null || senderId.isBlank()) throw new IllegalArgumentException("senderId mancante");
        if (receiverId == null || receiverId.isBlank()) throw new IllegalArgumentException("receiverId mancante");
        if (referenceId == null || referenceId.isBlank()) throw new IllegalArgumentException("referenceId mancante");
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("amount deve essere > 0");
    }
}
