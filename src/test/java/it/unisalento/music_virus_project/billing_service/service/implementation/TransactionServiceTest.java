package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Transaction;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionReferenceType;
import it.unisalento.music_virus_project.billing_service.domain.enums.TransactionType;
import it.unisalento.music_virus_project.billing_service.repositories.ITransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void recordFeePayment_whenOk_savesAndReturnsTransaction() {
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Transaction res = transactionService.recordFeePayment(
                "sender1",
                "receiver1",
                new BigDecimal("10.00"),
                "feePlan1",
                TransactionReferenceType.CONTRIBUTION
        );

        assertNotNull(res);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertEquals(TransactionType.FEE_PAYMENT, saved.getTransactionType());
        assertEquals(TransactionReferenceType.CONTRIBUTION, saved.getReferenceType());
        assertEquals("feePlan1", saved.getReferenceId());
        assertEquals("sender1", saved.getSenderId());
        assertEquals("receiver1", saved.getReceiverId());
        assertEquals(new BigDecimal("10.00"), saved.getAmount());
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
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        transactionService.recordContributionPayment(
                "fan1",
                "artist1",
                new BigDecimal("5.50"),
                "contrib1"
        );

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertEquals(TransactionType.CONTRIBUTION_PAYMENT, saved.getTransactionType());
        assertEquals(TransactionReferenceType.CONTRIBUTION, saved.getReferenceType());
        assertEquals("contrib1", saved.getReferenceId());
        assertEquals("fan1", saved.getSenderId());
        assertEquals("artist1", saved.getReceiverId());
        assertEquals(new BigDecimal("5.50"), saved.getAmount());
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
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        transactionService.recordRefund(
                "artist1",
                "fan1",
                new BigDecimal("3.00"),
                "refund1"
        );

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertEquals(TransactionType.REFUND, saved.getTransactionType());
        assertEquals(TransactionReferenceType.REFUND, saved.getReferenceType());
        assertEquals("refund1", saved.getReferenceId());
        assertEquals("artist1", saved.getSenderId());
        assertEquals("fan1", saved.getReceiverId());
        assertEquals(new BigDecimal("3.00"), saved.getAmount());
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
