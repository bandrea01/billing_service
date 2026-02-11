package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Contribution;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionStatus;
import it.unisalento.music_virus_project.billing_service.repositories.IContributionRepository;
import it.unisalento.music_virus_project.billing_service.service.ITransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContributionServiceTest {

    @Mock
    private IContributionRepository contributionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private ITransactionService transactionService;

    private ContributionService service;

    @BeforeEach
    void setUp() {
        service = new ContributionService(contributionRepository, accountService, transactionService);
    }

    @Test
    void createContribution_whenOk_debits_saves_recordsTransaction_andReturnsSaved() {
        String fundraisingId = "fund1";
        String fanUserId = "fan1";
        String artistId = "artist1";
        BigDecimal amount = new BigDecimal("10.00");

        // simulate repository returning entity with id
        Contribution saved = new Contribution();
        saved.setContributionId("c1");
        saved.setFundraisingId(fundraisingId);
        saved.setUserId(fanUserId);
        saved.setArtistId(artistId);
        saved.setAmount(amount);
        saved.setStatus(ContributionStatus.CAPTURED);

        when(contributionRepository.save(any(Contribution.class))).thenReturn(saved);

        Contribution result = service.createContribution(fundraisingId, fanUserId, artistId, amount);

        assertNotNull(result);
        assertEquals("c1", result.getContributionId());
        assertEquals(fundraisingId, result.getFundraisingId());
        assertEquals(fanUserId, result.getUserId());
        assertEquals(artistId, result.getArtistId());
        assertEquals(amount, result.getAmount());
        assertEquals(ContributionStatus.CAPTURED, result.getStatus());

        // verify call order: debit -> save -> record
        InOrder inOrder = inOrder(accountService, contributionRepository, transactionService);
        inOrder.verify(accountService).debit(eq(fanUserId), eq(amount));
        inOrder.verify(contributionRepository).save(any(Contribution.class));
        inOrder.verify(transactionService).recordContributionPayment(eq(fanUserId), eq(artistId), eq(amount), eq("c1"));

        // no rollback
        verify(accountService, never()).credit(anyString(), any());
        verify(contributionRepository, never()).deleteById(anyString());
    }

    @Test
    void createContribution_whenTransactionFails_rollsBackCredit_andDeletesContribution_thenRethrows() {
        String fundraisingId = "fund1";
        String fanUserId = "fan1";
        String artistId = "artist1";
        BigDecimal amount = new BigDecimal("10.00");

        Contribution saved = new Contribution();
        saved.setContributionId("c1");

        when(contributionRepository.save(any(Contribution.class))).thenReturn(saved);

        RuntimeException boom = new RuntimeException("tx failed");
        doThrow(boom).when(transactionService)
                .recordContributionPayment(eq(fanUserId), eq(artistId), eq(amount), eq("c1"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createContribution(fundraisingId, fanUserId, artistId, amount));

        assertSame(boom, ex);

        verify(accountService).debit(eq(fanUserId), eq(amount));
        verify(contributionRepository).save(any(Contribution.class));
        verify(transactionService).recordContributionPayment(eq(fanUserId), eq(artistId), eq(amount), eq("c1"));

        // rollback
        verify(accountService).credit(eq(fanUserId), eq(amount));
        verify(contributionRepository).deleteById(eq("c1"));
    }

    @Test
    void createContribution_whenSaveFails_rollsBackCredit_andRethrows_noDelete() {
        String fundraisingId = "fund1";
        String fanUserId = "fan1";
        String artistId = "artist1";
        BigDecimal amount = new BigDecimal("10.00");

        RuntimeException boom = new RuntimeException("save failed");
        when(contributionRepository.save(any(Contribution.class))).thenThrow(boom);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createContribution(fundraisingId, fanUserId, artistId, amount));

        assertSame(boom, ex);

        verify(accountService).debit(eq(fanUserId), eq(amount));
        verify(contributionRepository).save(any(Contribution.class));

        // rollback: credit attempted
        verify(accountService).credit(eq(fanUserId), eq(amount));

        // no delete because nothing saved / no id
        verify(contributionRepository, never()).deleteById(anyString());
        verify(transactionService, never()).recordContributionPayment(anyString(), anyString(), any(), anyString());
    }

    @Test
    void createContribution_whenRollbackCreditFails_stillAttemptsDelete_andRethrowsOriginal() {
        // Questo test copre il catch(ignored) sul rollback credit
        String fundraisingId = "fund1";
        String fanUserId = "fan1";
        String artistId = "artist1";
        BigDecimal amount = new BigDecimal("10.00");

        Contribution saved = new Contribution();
        saved.setContributionId("c1");
        when(contributionRepository.save(any(Contribution.class))).thenReturn(saved);

        RuntimeException original = new RuntimeException("tx failed");
        doThrow(original).when(transactionService)
                .recordContributionPayment(eq(fanUserId), eq(artistId), eq(amount), eq("c1"));

        doThrow(new RuntimeException("credit failed")).when(accountService).credit(eq(fanUserId), eq(amount));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createContribution(fundraisingId, fanUserId, artistId, amount));

        assertSame(original, ex);

        // delete still attempted
        verify(contributionRepository).deleteById("c1");
    }
}
