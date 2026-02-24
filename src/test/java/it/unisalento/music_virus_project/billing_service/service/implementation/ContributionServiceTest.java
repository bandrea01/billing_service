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

        verify(contributionRepository).save(any(Contribution.class));
        verify(transactionService).recordContributionPayment(eq(fanUserId), eq(artistId), eq(amount), eq("c1"));

        verify(contributionRepository).deleteById(eq("c1"));

        verify(contributionEventPublisher, never()).publishContributionAdded(any());
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

        verify(contributionRepository).save(any(Contribution.class));

        verify(contributionRepository, never()).deleteById(anyString());
        verify(transactionService, never()).recordContributionPayment(anyString(), anyString(), any(), anyString());
        verify(contributionEventPublisher, never()).publishContributionAdded(any());
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

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createContribution(fundraisingId, fanUserId, artistId, amount, visibility));

        // la classe rilancia l'eccezione originale del try/catch
        assertSame(original, ex);

        // anche se credit fallisce, deve comunque provare a cancellare
        verify(contributionRepository).deleteById(eq("c1"));
    }
}