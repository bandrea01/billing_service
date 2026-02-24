package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Transaction;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionReferenceType;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionType;
import it.unisalento.music_virus_project.billing_service.repositories.ITransactionRepository;
import it.unisalento.music_virus_project.billing_service.service.IAccountBalanceService;
import it.unisalento.music_virus_project.billing_service.service.IAccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private IAccountBalanceService accountBalanceService;

    @Mock
    private IAccountService accountService;

    @Mock
    private FeeService feeService;

    @InjectMocks
    private TransactionService transactionService;

    private void mockSaveAssigningId() {
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            try {
                Field f;
                try {
                    f = Transaction.class.getDeclaredField("transactionId");
                } catch (NoSuchFieldException ex) {
                    f = Transaction.class.getDeclaredField("id");
                }
                f.setAccessible(true);
                if (f.get(t) == null) {
                    f.set(t, "tx1");
                }
            } catch (Exception e) {
                throw new RuntimeException("Impossibile settare l'id su Transaction via reflection", e);
            }
            return t;
        });
    }

    /**
     * debit/credit NON sono void nel tuo progetto (altrimenti doNothing() andrebbe bene).
     * Il service non usa il return, quindi ritorniamo semplicemente null.
     */
    private void stubAccountBalanceOpsAsOk() {
        when(accountBalanceService.debitByUserId(anyString(), any(BigDecimal.class))).thenAnswer(inv -> null);
        when(accountBalanceService.creditByUserId(anyString(), any(BigDecimal.class))).thenAnswer(inv -> null);
    }

    @Test
    void recordFeePayment_whenOk_savesAndReturnsTransaction() {
        mockSaveAssigningId();
        stubAccountBalanceOpsAsOk();

        Transaction res = transactionService.recordFeePayment(
                "sender1",
                "receiver1",
                new BigDecimal("10.00"),
                "feePlan1",
                TransactionReferenceType.CONTRIBUTION
        );

        assertNotNull(res);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, atLeast(2)).save(captor.capture());

        List<Transaction> allSaved = captor.getAllValues();
        Transaction firstSaved = allSaved.get(0);

        assertEquals(TransactionType.FEE_PAYMENT, firstSaved.getTransactionType());
        assertEquals(TransactionReferenceType.CONTRIBUTION, firstSaved.getReferenceType());
        assertEquals("feePlan1", firstSaved.getReferenceId());
        assertEquals("sender1", firstSaved.getSenderId());
        assertEquals("receiver1", firstSaved.getReceiverId());
        assertEquals(new BigDecimal("10.00"), firstSaved.getAmount());

        verify(accountBalanceService).debitByUserId("sender1", new BigDecimal("10.00"));
        verify(accountBalanceService).creditByUserId("receiver1", new BigDecimal("10.00"));
    }

    @Test
    void recordFeePayment_whenSenderMissing_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordFeePayment(
                        "  ",
                        "receiver1",
                        new BigDecimal("10.00"),
                        "feePlan1",
                        TransactionReferenceType.CONTRIBUTION
                )
        );
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordFeePayment_whenReceiverMissing_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordFeePayment(
                        "sender1",
                        "",
                        new BigDecimal("10.00"),
                        "feePlan1",
                        TransactionReferenceType.CONTRIBUTION
                )
        );
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordFeePayment_whenReferenceMissing_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordFeePayment(
                        "sender1",
                        "receiver1",
                        new BigDecimal("10.00"),
                        "   ",
                        TransactionReferenceType.CONTRIBUTION
                )
        );
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordFeePayment_whenAmountNull_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordFeePayment(
                        "sender1",
                        "receiver1",
                        null,
                        "feePlan1",
                        TransactionReferenceType.CONTRIBUTION
                )
        );
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordFeePayment_whenAmountZeroOrNegative_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordFeePayment(
                        "sender1",
                        "receiver1",
                        BigDecimal.ZERO,
                        "feePlan1",
                        TransactionReferenceType.CONTRIBUTION
                )
        );
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordFeePayment(
                        "sender1",
                        "receiver1",
                        new BigDecimal("-1"),
                        "feePlan1",
                        TransactionReferenceType.CONTRIBUTION
                )
        );
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordContributionPayment_whenOk_savesTransaction() {
        mockSaveAssigningId();
        when(accountBalanceService.debitByUserId(anyString(), any(BigDecimal.class))).thenAnswer(inv -> null);

        transactionService.recordContributionPayment(
                "fan1",
                "artist1",
                new BigDecimal("5.50"),
                "contrib1"
        );

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertNotNull(saved);

        assertEquals(TransactionType.CONTRIBUTION_PAYMENT, saved.getTransactionType());
        assertEquals(TransactionReferenceType.CONTRIBUTION, saved.getReferenceType());
        assertEquals("contrib1", saved.getReferenceId());
        assertEquals("fan1", saved.getSenderId());
        assertEquals("artist1", saved.getReceiverId());
        assertEquals(new BigDecimal("5.50"), saved.getAmount());

        verify(accountBalanceService).debitByUserId("fan1", new BigDecimal("5.50"));
    }

    @Test
    void recordContributionPayment_whenInvalidParams_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordContributionPayment(null, "artist1", new BigDecimal("1"), "c1")
        );
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordContributionPayment("fan1", " ", new BigDecimal("1"), "c1")
        );
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordContributionPayment("fan1", "artist1", new BigDecimal("0"), "c1")
        );
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordContributionPayment("fan1", "artist1", new BigDecimal("1"), " ")
        );

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void recordRefund_whenOk_savesTransaction() {
        mockSaveAssigningId();

        transactionService.recordRefund(
                "artist1",
                "fan1",
                new BigDecimal("3.00"),
                "refund1"
        );

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertNotNull(saved);

        assertEquals(TransactionType.REFUND, saved.getTransactionType());
        assertEquals(TransactionReferenceType.REFUND, saved.getReferenceType());
        assertEquals("refund1", saved.getReferenceId());
        assertEquals("artist1", saved.getSenderId());
        assertEquals("fan1", saved.getReceiverId());
        assertEquals(new BigDecimal("3.00"), saved.getAmount());

        verify(accountBalanceService).creditByUserId("fan1", new BigDecimal("3.00"));
    }

    @Test
    void recordRefund_whenInvalidParams_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordRefund("", "fan1", new BigDecimal("1"), "r1")
        );
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordRefund("artist1", null, new BigDecimal("1"), "r1")
        );
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordRefund("artist1", "fan1", null, "r1")
        );
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.recordRefund("artist1", "fan1", new BigDecimal("1"), "")
        );

        verify(transactionRepository, never()).save(any());
    }
}