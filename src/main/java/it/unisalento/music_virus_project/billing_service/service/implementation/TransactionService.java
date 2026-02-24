package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Transaction;
import it.unisalento.music_virus_project.billing_service.domain.enums.TaxEnum;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionReferenceType;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionType;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.TaxListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.TaxResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.transaction.TransactionListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.transaction.TransactionResponseDTO;
import it.unisalento.music_virus_project.billing_service.repositories.ITransactionRepository;
import it.unisalento.music_virus_project.billing_service.service.IAccountBalanceService;
import it.unisalento.music_virus_project.billing_service.service.IAccountService;
import it.unisalento.music_virus_project.billing_service.service.ITransactionService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@Service
public class TransactionService implements ITransactionService {

    final String EVENT_PAYMENT_SENDER = "EventPaymentSystem";

    private final ITransactionRepository transactionRepository;
    private final IAccountBalanceService accountBalanceService;
    private final IAccountService accountService;
    private final FeeService feeService;

    public TransactionService(
            ITransactionRepository transactionRepository,
            IAccountBalanceService accountBalanceService,
            IAccountService accountService,
            FeeService feeService
    ) {
        this.transactionRepository = transactionRepository;
        this.accountBalanceService = accountBalanceService;
        this.accountService = accountService;
        this.feeService = feeService;
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

        Transaction eventPaymentTransaction = new Transaction(
                TransactionType.EVENT_PAYMENT,
                TransactionReferenceType.EVENT,
                eventId,
                EVENT_PAYMENT_SENDER,
                receiverId,
                amount
        );
        eventPaymentTransaction = transactionRepository.save(eventPaymentTransaction);
        System.out.println("Transazione di pagamento evento registrata con ID: " + eventPaymentTransaction.getTransactionId());

        if (eventPaymentTransaction.getTransactionId() == null) {
            throw new AmqpRejectAndDontRequeueException("Errore durante la registrazione della transazione di pagamento evento");
        }

        // credit receiver's account
        try {
            accountBalanceService.creditByUserId(receiverId, amount);
            System.out.println("Conto del destinatario accreditato con successo per l'importo: " + amount);
        } catch (Exception e) {
            transactionRepository.deleteById(eventPaymentTransaction.getTransactionId());
            throw new AmqpRejectAndDontRequeueException("Errore durante l'accredito del conto del destinatario: " + e.getMessage());
        }

        // Fee payment to admin
        AccountResponseDTO adminAccount = accountService.getAdminAccount();
        System.out.println("Account amministratore recuperato: " + (adminAccount != null ? adminAccount.getUserId() : "null"));
        if (adminAccount == null) {
            transactionRepository.deleteById(eventPaymentTransaction.getTransactionId());
            throw new AmqpRejectAndDontRequeueException("Errore: account amministratore non trovato");
        }

        TaxListResponseDTO taxList = feeService.getTaxList();
        System.out.println("Lista delle tasse recuperata, numero di tasse: " + taxList.getTaxes().size());
        if (!taxList.getTaxes().isEmpty()) {
            System.out.println("Taxes found: " + taxList.getTaxes().size());
            TaxResponseDTO tax = taxList.getTaxes().get(0);
            System.out.println("Tax name: " + tax.getTaxName());
            if (tax.getTaxName().equals(TaxEnum.EVENT_TAX.name())) {
                BigDecimal taxPercentage = tax.getPercentageOnTotal();
                System.out.println("Tax percentage: " + taxPercentage);
                BigDecimal taxAmount = amount.multiply(taxPercentage).divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);
                System.out.println( "Calculated tax amount: " + taxAmount);
                try {
                    recordFeePayment(
                            receiverId, //artist
                            adminAccount.getUserId(),
                            taxAmount,
                            tax.getFeePlanId(),
                            TransactionReferenceType.FEE_PLAN
                    );
                } catch (Exception e) {
                    throw new AmqpRejectAndDontRequeueException("Errore durante la registrazione del pagamento della tassa: " + e.getMessage());
                }
            }
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

        try {
            transaction = transactionRepository.save(transaction);
            if (transaction.getTransactionId() == null) {
                System.out.println("Errore: transactionId è null dopo il salvataggio della transazione di pagamento tassa");
                throw new AmqpRejectAndDontRequeueException("Errore durante la registrazione della transazione di pagamento tassa");
            }
        } catch (Exception e) {
            System.out.println("Eccezione durante il salvataggio della transazione di pagamento tassa: " + e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Errore durante la registrazione della transazione di pagamento tassa: " + e.getMessage());
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

        // no debit sender's account -> contribution is accredited on event confirmation
        // credit receiver's account
        try {
            accountBalanceService.creditByUserId(receiverId, amount);
        } catch (Exception e) {
            transactionRepository.deleteById(transaction.getTransactionId());
            throw new RuntimeException("Errore durante l'accredito del conto del destinatario: " + e.getMessage());
        }
    }

    @Override
    public TransactionListResponseDTO getLast10TransactionsByUserId(String userId) {
        return mapToDTOList(transactionRepository.findTop10BySenderIdOrReceiverIdOrderByCreatedAt(userId, userId));
    }

    @Override
    public TransactionListResponseDTO getAllTransactionsByUserId(String userId) {
        return mapToDTOList(transactionRepository.findBySenderIdOrReceiverIdOrderByCreatedAt(userId, userId));
    }

    @Override
    public TransactionListResponseDTO getLast10IncomingTransactionsByUserId(String userId) {
        return mapToDTOList(transactionRepository.findTop10ByReceiverIdOrderByCreatedAt(userId));
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
                transaction.getReceiverId(),
                transaction.getAmount(),
                transaction.getCreatedAt()
        );
    }
}
