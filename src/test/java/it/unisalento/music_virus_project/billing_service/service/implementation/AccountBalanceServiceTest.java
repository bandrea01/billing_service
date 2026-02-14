package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Account;
import it.unisalento.music_virus_project.billing_service.exceptions.InsufficentBalanceException;
import it.unisalento.music_virus_project.billing_service.exceptions.NotFoundException;
import it.unisalento.music_virus_project.billing_service.repositories.IAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountBalanceServiceTest {

    @Mock
    private IAccountRepository accountRepository;

    private AccountBalanceService service;

    @BeforeEach
    void setUp() {
        service = new AccountBalanceService(accountRepository);
    }

    @Test
    void debitByUserId_whenAmountNull_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> service.debitByUserId("u1", null));
        verifyNoInteractions(accountRepository);
    }

    @Test
    void debitByUserId_whenAmountZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> service.debitByUserId("u1", BigDecimal.ZERO));
        verifyNoInteractions(accountRepository);
    }

    @Test
    void debitByUserId_whenAmountNegative_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> service.debitByUserId("u1", new BigDecimal("-1")));
        verifyNoInteractions(accountRepository);
    }

    @Test
    void debitByUserId_whenMongoReturnsNull_throwsInsufficientBalance() {
        when(accountRepository.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Account.class)
        )).thenReturn(null);

        InsufficentBalanceException ex = assertThrows(
                InsufficentBalanceException.class,
                () -> service.debitByUserId("u1", new BigDecimal("10"))
        );

        // messaggio (nel tuo codice manca uno spazio, lo controllo "contains")
        assertTrue(ex.getMessage().contains("saldo insufficiente"));
        assertTrue(ex.getMessage().contains("u1"));

        verify(mongoTemplate, times(1)).findAndModify(
                any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Account.class)
        );
    }

    @Test
    void debitByUserId_whenOk_returnsUpdatedAccount_andUsesNegativeInc() {
        Account updated = new Account();
        // se Account ha setter: updated.setUserId("u1"); updated.setBalance(...)
        when(mongoTemplate.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Account.class)
        )).thenReturn(updated);

        Account result = service.debitByUserId("u1", new BigDecimal("10"));

        assertSame(updated, result);

        // catturo Update per verificare che inc sia negativo
        ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);

        verify(mongoTemplate).findAndModify(
                any(Query.class),
                updateCaptor.capture(),
                any(FindAndModifyOptions.class),
                eq(Account.class)
        );

        Update usedUpdate = updateCaptor.getValue();
        String updateStr = usedUpdate.getUpdateObject().toJson(); // {"$inc":{"balance":-10}}

        assertTrue(updateStr.contains("\"balance\""));
        assertTrue(updateStr.contains("-10"));
    }

    // -------------------------
    // creditByUserId
    // -------------------------

    @Test
    void creditByUserId_whenAmountNull_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> service.creditByUserId("u1", null));
        verifyNoInteractions(mongoTemplate);
    }

    @Test
    void creditByUserId_whenAmountZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> service.creditByUserId("u1", BigDecimal.ZERO));
        verifyNoInteractions(mongoTemplate);
    }

    @Test
    void creditByUserId_whenAmountNegative_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> service.creditByUserId("u1", new BigDecimal("-5")));
        verifyNoInteractions(mongoTemplate);
    }

    @Test
    void creditByUserId_whenMongoReturnsNull_throwsNotFound() {
        when(mongoTemplate.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Account.class)
        )).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.creditByUserId("u1", new BigDecimal("10"))
        );

        assertTrue(ex.getMessage().contains("account non trovato"));
        assertTrue(ex.getMessage().contains("u1"));

        verify(mongoTemplate, times(1)).findAndModify(
                any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Account.class)
        );
    }

    @Test
    void creditByUserId_whenOk_returnsUpdatedAccount_andUsesPositiveInc() {
        Account updated = new Account();
        when(mongoTemplate.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Account.class)
        )).thenReturn(updated);

        Account result = service.creditByUserId("u1", new BigDecimal("15"));

        assertSame(updated, result);

        ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);

        verify(mongoTemplate).findAndModify(
                any(Query.class),
                updateCaptor.capture(),
                any(FindAndModifyOptions.class),
                eq(Account.class)
        );

        String updateStr = updateCaptor.getValue().getUpdateObject().toJson(); // {"$inc":{"balance":15}}
        assertTrue(updateStr.contains("\"balance\""));
        assertTrue(updateStr.contains("15"));
        assertFalse(updateStr.contains("-15"));
    }
}
