package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Contribution;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionVisibility;
import it.unisalento.music_virus_project.billing_service.messaging.publish.ContributionEventPublisher;
import it.unisalento.music_virus_project.billing_service.repositories.IContributionRepository;
import it.unisalento.music_virus_project.billing_service.service.ITransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContributionServiceTest {

    @Mock
    private IContributionRepository contributionRepository;
    @Mock
    private ContributionEventPublisher contributionEventPublisher;
    @Mock
    private AccountBalanceService accountBalanceService;
    @Mock
    private ITransactionService transactionService;

    private ContributionService service;

    @BeforeEach
    void setUp() {
        service = new ContributionService(contributionRepository, transactionService, contributionEventPublisher, accountBalanceService);
    }

    @Test
    void createContribution_whenTransactionFails_rollsBackCredit_andDeletesContribution_thenRethrows() {
        String fundraisingId = "fund1";
        String fanUserId = "fan1";
        String artistId = "artist1";
        BigDecimal amount = new BigDecimal("10.00");
        ContributionVisibility visibility = ContributionVisibility.PUBLIC;

        Contribution saved = new Contribution();
        saved.setContributionId("c1");

        when(contributionRepository.save(any(Contribution.class))).thenReturn(saved);

        RuntimeException boom = new RuntimeException("tx failed");
        doThrow(boom).when(transactionService)
                .recordContributionPayment(eq(fanUserId), eq(artistId), eq(amount), eq("c1"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createContribution(fundraisingId, fanUserId, artistId, amount, visibility));

        assertSame(boom, ex);

        verify(accountBalanceService).debitByUserId(eq(fanUserId), eq(amount));
        verify(contributionRepository).save(any(Contribution.class));
        verify(transactionService).recordContributionPayment(eq(fanUserId), eq(artistId), eq(amount), eq("c1"));

        // rollback
        verify(accountBalanceService).creditByUserId(eq(fanUserId), eq(amount));
        verify(contributionRepository).deleteById(eq("c1"));
    }

    @Test
    void createContribution_whenSaveFails_rollsBackCredit_andRethrows_noDelete() {
        String fundraisingId = "fund1";
        String fanUserId = "fan1";
        String artistId = "artist1";
        BigDecimal amount = new BigDecimal("10.00");
        ContributionVisibility visibility = ContributionVisibility.PUBLIC;

        RuntimeException boom = new RuntimeException("save failed");
        when(contributionRepository.save(any(Contribution.class))).thenThrow(boom);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createContribution(fundraisingId, fanUserId, artistId, amount, visibility));

        assertSame(boom, ex);

        verify(accountBalanceService).debitByUserId(eq(fanUserId), eq(amount));
        verify(contributionRepository).save(any(Contribution.class));

        // rollback: credit attempted
        verify(accountBalanceService).creditByUserId(eq(fanUserId), eq(amount));

        // no delete because nothing saved / no id
        verify(contributionRepository, never()).deleteById(anyString());
        verify(transactionService, never()).recordContributionPayment(anyString(), anyString(), any(), anyString());
    }

    @Test
    void createContribution_whenRollbackCreditFails_stillAttemptsDelete_andRethrowsOriginal() {
        String fundraisingId = "fund1";
        String fanUserId = "fan1";
        String artistId = "artist1";
        BigDecimal amount = new BigDecimal("10.00");
        ContributionVisibility visibility = ContributionVisibility.PUBLIC;

        Contribution saved = new Contribution();
        saved.setContributionId("c1");
        when(contributionRepository.save(any(Contribution.class))).thenReturn(saved);

        RuntimeException original = new RuntimeException("tx failed");
        doThrow(original).when(transactionService)
                .recordContributionPayment(eq(fanUserId), eq(artistId), eq(amount), eq("c1"));

        doThrow(new RuntimeException("credit failed")).when(accountBalanceService).creditByUserId(eq(fanUserId), eq(amount));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createContribution(fundraisingId, fanUserId, artistId, amount, visibility));

        assertSame(original, ex);

        // delete still attempted
        verify(contributionRepository).deleteById("c1");
    }
}
