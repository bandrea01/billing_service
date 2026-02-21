package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Account;
import it.unisalento.music_virus_project.billing_service.repositories.IAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountBalanceServiceTest {

    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private AccountBalanceService service;

    @BeforeEach
    void setUp() {
        service = new AccountBalanceService(accountRepository);
    }

    @Test
    void debitByUserId_whenAmountNull_throwsIllegalArgument() {
        when(accountRepository.findByUserId("userId1"))
                .thenReturn(Optional.of(new Account()));

        assertThrows(IllegalArgumentException.class,
                () -> service.debitByUserId("userId1", null));

        verify(accountRepository).findByUserId("userId1");
        verify(accountRepository, never()).save(any());
    }

    @Test
    void debitByUserId_whenAmountZero_throwsIllegalArgument() {
        when(accountRepository.findByUserId("userId1"))
                .thenReturn(Optional.of(new Account()));

        assertThrows(IllegalArgumentException.class,
                () -> service.debitByUserId("userId1", BigDecimal.ZERO));

        verify(accountRepository).findByUserId("userId1");
        verify(accountRepository, never()).save(any());
    }

    @Test
    void debitByUserId_whenAmountNegative_throwsIllegalArgument() {
        when(accountRepository.findByUserId("userId1"))
                .thenReturn(Optional.of(new Account()));

        assertThrows(IllegalArgumentException.class,
                () -> service.debitByUserId("userId1", new BigDecimal("-1")));

        verify(accountRepository).findByUserId("userId1");
        verify(accountRepository, never()).save(any());
    }

    @Test
    void creditByUserId_whenAmountNull_throwsIllegalArgument() {
        when(accountRepository.findByUserId("userId1"))
                .thenReturn(Optional.of(new Account()));

        assertThrows(IllegalArgumentException.class,
                () -> service.creditByUserId("userId1", null));

        verify(accountRepository).findByUserId("userId1");
        verify(accountRepository, never()).save(any());
    }

    @Test
    void creditByUserId_whenAmountZero_throwsIllegalArgument() {
        when(accountRepository.findByUserId("userId1"))
                .thenReturn(Optional.of(new Account()));

        assertThrows(IllegalArgumentException.class,
                () -> service.creditByUserId("userId1", BigDecimal.ZERO));

        verify(accountRepository).findByUserId("userId1");
        verify(accountRepository, never()).save(any());
    }

    @Test
    void creditByUserId_whenAmountNegative_throwsIllegalArgument() {
        when(accountRepository.findByUserId("userId1"))
                .thenReturn(Optional.of(new Account()));

        assertThrows(IllegalArgumentException.class,
                () -> service.creditByUserId("userId1", new BigDecimal("-1")));

        verify(accountRepository).findByUserId("userId1");
        verify(accountRepository, never()).save(any());
    }
}
