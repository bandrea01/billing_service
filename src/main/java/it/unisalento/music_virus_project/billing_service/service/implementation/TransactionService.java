package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Transaction;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionReferenceType;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionType;
import it.unisalento.music_virus_project.billing_service.dto.transaction.TransactionListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.transaction.TransactionResponseDTO;
import it.unisalento.music_virus_project.billing_service.repositories.ITransactionRepository;
import it.unisalento.music_virus_project.billing_service.service.IAccountBalanceService;
import it.unisalento.music_virus_project.billing_service.service.ITransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService implements ITransactionService {

    final String EVENT_PAYMENT_SENDER = "EventPaymentSystem";

    private final ITransactionRepository transactionRepository;
    private final IAccountBalanceService accountBalanceService;

    public TransactionService(ITransactionRepository transactionRepository, IAccountBalanceService accountBalanceService) {
        this.transactionRepository = transactionRepository;
        this.accountBalanceService = accountBalanceService;
    }

    @Override
    public TransactionListResponseDTO getTransactionsByUserId(String userId) {
        return mapToDTOList(transactionRepository.findByReceiverId(userId));
    }

    @Override
    public void recordEventPayment(
            String eventId,
            String receiverId,
            BigDecimal amount
    ) {
        validate(EVENT_PAYMENT_SENDER, receiverId, amount, eventId);

        Transaction transaction = new Transaction(
                TransactionType.EVENT_PAYMENT,
                TransactionReferenceType.EVENT,
                eventId,
                EVENT_PAYMENT_SENDER,
                receiverId,
                amount
        );
        transactionRepository.save(transaction);

        if (transaction.getTransactionId() == null) {
            throw new RuntimeException("Errore durante la registrazione della transazione di pagamento evento");
        }

        // Debit already did during contribution payment from the fan
        // credit receiver's account
        try {
            accountBalanceService.creditByUserId(receiverId, amount);
        } catch (Exception e) {
            transactionRepository.deleteById(transaction.getTransactionId());
            throw new RuntimeException("Errore durante l'accredito del conto del destinatario: " + e.getMessage());
        }
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

        Transaction transaction = new Transaction(
                TransactionType.FEE_PAYMENT,
                feeReferenceType,
                feePlanId,
                senderId,
                receiverId,
                amount
        );

        if (transaction.getTransactionId() == null) {
            throw new RuntimeException("Errore durante la registrazione della transazione di pagamento tassa");
        }

        // debit sender's account
        try {
            accountBalanceService.debitByUserId(senderId, amount);
        } catch (Exception e) {
            transactionRepository.deleteById(transaction.getTransactionId());
            throw new RuntimeException(e.getMessage());
        }
        // credit receiver's account
        try {
            accountBalanceService.creditByUserId(receiverId, amount);
        } catch (Exception e) {
            transactionRepository.deleteById(transaction.getTransactionId());
            throw new RuntimeException("Errore durante l'accredito del conto del destinatario: " + e.getMessage());
        }

        return transactionRepository.save(transaction);
    }

    @Override
    public void recordContributionPayment(
            String senderId,
            String receiverId,
            BigDecimal amount,
            String contributionId
    ) {
        validate(senderId, receiverId, amount, contributionId);

        Transaction transaction = new Transaction(
                TransactionType.CONTRIBUTION_PAYMENT,
                TransactionReferenceType.CONTRIBUTION,
                contributionId,
                senderId,
                receiverId,
                amount
        );
        transactionRepository.save(transaction);

        if (transaction.getTransactionId() == null) {
            throw new RuntimeException("Errore durante la registrazione della transazione di pagamento contributo");
        }

        // debit sender's account
        try {
            accountBalanceService.debitByUserId(senderId, amount);
        } catch (Exception e) {
            transactionRepository.deleteById(transaction.getTransactionId());
            throw new RuntimeException("Errore durante l'addebito del conto del mittente: " + e.getMessage());
        }

        // credit receiver on event confirmation

    }

    @Override
    public void recordRefund(
            String senderId,
            String receiverId,
            BigDecimal amount,
            String refundId
    ) {
        validate(senderId, receiverId, amount, refundId);

        Transaction transaction = new Transaction(
                TransactionType.REFUND,
                TransactionReferenceType.REFUND,
                refundId,
                senderId,
                receiverId,
                amount
        );
        transactionRepository.save(transaction);

        if (transaction.getTransactionId() == null) {
            throw new RuntimeException("Errore durante la registrazione della transazione di rimborso");
        }

        // debit sender's account
        try {
            accountBalanceService.debitByUserId(senderId, amount);
        } catch (Exception e) {
            transactionRepository.deleteById(transaction.getTransactionId());
            throw new RuntimeException(e.getMessage());
        }
        // credit receiver's account
        try {
            accountBalanceService.creditByUserId(receiverId, amount);
        } catch (Exception e) {
            transactionRepository.deleteById(transaction.getTransactionId());
            throw new RuntimeException("Errore durante l'accredito del conto del destinatario: " + e.getMessage());
        }
    }

    private void validate(String senderId, String receiverId, BigDecimal amount, String referenceId) {
        if (senderId == null || senderId.isBlank()) throw new IllegalArgumentException("senderId mancante");
        if (receiverId == null || receiverId.isBlank()) throw new IllegalArgumentException("receiverId mancante");
        if (referenceId == null || referenceId.isBlank()) throw new IllegalArgumentException("referenceId mancante");
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("amount deve essere > 0");
    }

    private TransactionListResponseDTO mapToDTOList(List<Transaction> transactions) {
        TransactionListResponseDTO dto = new TransactionListResponseDTO();
        for (Transaction tx : transactions) {
            dto.getTransactions().add(mapToDTO(tx));
        }
        return dto;
    }

    private TransactionResponseDTO mapToDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getTransactionId(),
                transaction.getTransactionType(),
                transaction.getSenderId(),
                transaction.getAmount()
        );
    }
}
