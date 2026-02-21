package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Account;
import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.enums.AccountStatus;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountUpdateRequestDTO;
import it.unisalento.music_virus_project.billing_service.exceptions.NotFoundException;
import it.unisalento.music_virus_project.billing_service.repositories.IAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private AccountBalanceService balanceService;

    private AccountService service;

    @BeforeEach
    void setUp() {
        service = new AccountService(accountRepository, balanceService);
    }

    private static Account account(String accountId, String userId, Role role, AccountStatus status, BigDecimal balance) {
        Account a = new Account();
        a.setAccountId(accountId);
        a.setUserId(userId);
        a.setRole(role);
        a.setStatus(status);
        a.setBalance(balance);
        return a;
    }

    @Test
    void getAllAccounts_returnsListDTO() {
        when(accountRepository.findAll()).thenReturn(List.of(
                account("a1", "u1", Role.FAN, AccountStatus.ACTIVE, new BigDecimal("10")),
                account("a2", "u2", Role.ARTIST, AccountStatus.SUSPENDED, new BigDecimal("0"))
        ));

        AccountListResponseDTO dto = service.getAllAccounts();

        assertNotNull(dto);
        assertNotNull(dto.getAccounts());
        assertEquals(2, dto.getAccounts().size());
        assertEquals("a1", dto.getAccounts().get(0).getAccountId());
        assertEquals("u2", dto.getAccounts().get(1).getUserId());

        verify(accountRepository).findAll();
        verifyNoInteractions(balanceService);
    }

    @Test
    void getAccountById_whenFound_returnsDTO() {
        when(accountRepository.findByAccountId("a1"))
                .thenReturn(Optional.of(account("a1", "u1", Role.FAN, AccountStatus.ACTIVE, new BigDecimal("10"))));

        AccountResponseDTO dto = service.getAccountById("a1");

        assertEquals("a1", dto.getAccountId());
        assertEquals("u1", dto.getUserId());
        assertEquals(new BigDecimal("10"), dto.getBalance());
        assertEquals(AccountStatus.ACTIVE, dto.getStatus());

        verify(accountRepository).findByAccountId("a1");
        verifyNoInteractions(balanceService);
    }

    @Test
    void getAccountById_whenNotFound_throws() {
        when(accountRepository.findByAccountId("a1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getAccountById("a1"));

        verify(accountRepository).findByAccountId("a1");
        verifyNoInteractions(balanceService);
    }

    @Test
    void getAccountByUserId_whenFound_returnsDTO() {
        when(accountRepository.findByUserId("u1"))
                .thenReturn(Optional.of(account("a1", "u1", Role.FAN, AccountStatus.ACTIVE, new BigDecimal("5"))));

        AccountResponseDTO dto = service.getAccountByUserId("u1");

        assertEquals("a1", dto.getAccountId());
        assertEquals("u1", dto.getUserId());
        assertEquals(new BigDecimal("5"), dto.getBalance());

        verify(accountRepository).findByUserId("u1");
        verifyNoInteractions(balanceService);
    }

    @Test
    void getAccountByUserId_whenNotFound_throws() {
        when(accountRepository.findByUserId("u1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getAccountByUserId("u1"));

        verify(accountRepository).findByUserId("u1");
        verifyNoInteractions(balanceService);
    }

    @Test
    void getAdminAccount_whenFound_returnsDTO() {
        when(accountRepository.findFirstByRole(Role.ADMIN))
                .thenReturn(Optional.of(account("adminAcc", "adminUser", Role.ADMIN, AccountStatus.ACTIVE, new BigDecimal("99"))));

        AccountResponseDTO dto = service.getAdminAccount();

        assertEquals("adminAcc", dto.getAccountId());
        assertEquals("adminUser", dto.getUserId());
        assertEquals(new BigDecimal("99"), dto.getBalance());

        verify(accountRepository).findFirstByRole(Role.ADMIN);
        verifyNoInteractions(balanceService);
    }

    @Test
    void getAdminAccount_whenNotFound_throws() {
        when(accountRepository.findFirstByRole(Role.ADMIN)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getAdminAccount());

        verify(accountRepository).findFirstByRole(Role.ADMIN);
        verifyNoInteractions(balanceService);
    }

    @Test
    void createAccount_setsDefaults_andSaves() {
        Account saved = account("acc1", "u1", Role.ARTIST, AccountStatus.ACTIVE, BigDecimal.ZERO);
        when(accountRepository.save(any(Account.class))).thenReturn(saved);

        AccountResponseDTO dto = service.createAccount("u1", Role.ARTIST);

        assertEquals("acc1", dto.getAccountId());
        assertEquals("u1", dto.getUserId());
        assertEquals(BigDecimal.ZERO, dto.getBalance());
        assertEquals(AccountStatus.ACTIVE, dto.getStatus());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        Account toSave = captor.getValue();
        assertEquals("u1", toSave.getUserId());
        assertEquals(Role.ARTIST, toSave.getRole());
        assertEquals(AccountStatus.ACTIVE, toSave.getStatus());
        assertEquals(BigDecimal.ZERO, toSave.getBalance());

        verifyNoInteractions(balanceService);
    }

    @Test
    void debit_delegatesToBalanceService() {
        balanceService.debitByUserId("u1", new BigDecimal("10"));
        verify(balanceService).debitByUserId("u1", new BigDecimal("10"));
        verifyNoInteractions(accountRepository);
    }

    @Test
    void credit_delegatesToBalanceService() {
        balanceService.creditByUserId("u1", new BigDecimal("10"));
        verify(balanceService).creditByUserId("u1", new BigDecimal("10"));
        verifyNoInteractions(accountRepository);
    }

    @Test
    void depositByUserId_returnsMappedDTO() {
        Account updated = account("a1", "u1", Role.FAN, AccountStatus.ACTIVE, new BigDecimal("25"));
        when(balanceService.creditByUserId("u1", new BigDecimal("20"))).thenReturn(updated);

        AccountResponseDTO dto = service.depositByUserId("u1", new BigDecimal("20"));

        assertEquals("a1", dto.getAccountId());
        assertEquals("u1", dto.getUserId());
        assertEquals(new BigDecimal("25"), dto.getBalance());
        assertEquals(AccountStatus.ACTIVE, dto.getStatus());

        verify(balanceService).creditByUserId("u1", new BigDecimal("20"));
        verifyNoInteractions(accountRepository);
    }

    @Test
    void depositOnAdminAccount_whenAdminExists_depositsOnAdminUserId() {
        Account admin = account("adminAcc", "adminUser", Role.ADMIN, AccountStatus.ACTIVE, new BigDecimal("100"));
        when(accountRepository.findFirstByRole(Role.ADMIN)).thenReturn(Optional.of(admin));

        Account updated = account("adminAcc", "adminUser", Role.ADMIN, AccountStatus.ACTIVE, new BigDecimal("120"));
        when(balanceService.creditByUserId("adminUser", new BigDecimal("20"))).thenReturn(updated);

        AccountResponseDTO dto = service.depositOnAdminAccount(new BigDecimal("20"));

        assertEquals("adminAcc", dto.getAccountId());
        assertEquals("adminUser", dto.getUserId());
        assertEquals(new BigDecimal("120"), dto.getBalance());

        verify(accountRepository).findFirstByRole(Role.ADMIN);
        verify(balanceService).creditByUserId("adminUser", new BigDecimal("20"));
    }

    @Test
    void depositOnAdminAccount_whenAdminMissing_throws() {
        when(accountRepository.findFirstByRole(Role.ADMIN)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.depositOnAdminAccount(new BigDecimal("10")));

        verify(accountRepository).findFirstByRole(Role.ADMIN);
        verifyNoInteractions(balanceService);
    }

    @Test
    void updateAccount_updatesStatusAndBalance_whenProvided() {
        Account existing = account("a1", "u1", Role.FAN, AccountStatus.ACTIVE, new BigDecimal("10"));
        when(accountRepository.findByUserId("u1")).thenReturn(Optional.of(existing));

        Account saved = account("a1", "u1", Role.FAN, AccountStatus.SUSPENDED, new BigDecimal("999"));
        when(accountRepository.save(any(Account.class))).thenReturn(saved);

        AccountUpdateRequestDTO req = new AccountUpdateRequestDTO();
        req.setAccountStatus(AccountStatus.SUSPENDED);
        req.setAccountBalance(new BigDecimal("999"));

        AccountResponseDTO dto = service.updateAccount("u1", req);

        assertEquals("a1", dto.getAccountId());
        assertEquals("u1", dto.getUserId());
        assertEquals(AccountStatus.SUSPENDED, dto.getStatus());
        assertEquals(new BigDecimal("999"), dto.getBalance());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        assertEquals(AccountStatus.SUSPENDED, captor.getValue().getStatus());
        assertEquals(new BigDecimal("999"), captor.getValue().getBalance());

        verify(accountRepository).findByUserId("u1");
        verifyNoInteractions(balanceService);
    }

    @Test
    void updateAccount_whenOnlyStatusProvided_updatesOnlyStatus() {
        Account existing = account("a1", "u1", Role.FAN, AccountStatus.ACTIVE, new BigDecimal("10"));
        when(accountRepository.findByUserId("u1")).thenReturn(Optional.of(existing));

        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        AccountUpdateRequestDTO req = new AccountUpdateRequestDTO();
        req.setAccountStatus(AccountStatus.CLOSED);

        AccountResponseDTO dto = service.updateAccount("u1", req);

        assertEquals(AccountStatus.CLOSED, dto.getStatus());
        assertEquals(new BigDecimal("10"), dto.getBalance());

        verify(accountRepository).findByUserId("u1");
        verify(accountRepository).save(any(Account.class));
        verifyNoInteractions(balanceService);
    }

    @Test
    void updateAccount_whenNotFound_throws() {
        when(accountRepository.findByUserId("u1")).thenReturn(Optional.empty());

        AccountUpdateRequestDTO req = new AccountUpdateRequestDTO();
        req.setAccountStatus(AccountStatus.SUSPENDED);

        assertThrows(NotFoundException.class, () -> service.updateAccount("u1", req));

        verify(accountRepository).findByUserId("u1");
        verify(accountRepository, never()).save(any());
        verifyNoInteractions(balanceService);
    }

    @Test
    void closeAccountById_setsClosed() {
        Account existing = account("a1", "u1", Role.FAN, AccountStatus.ACTIVE, BigDecimal.ZERO);
        when(accountRepository.findByAccountId("a1")).thenReturn(Optional.of(existing));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        AccountResponseDTO dto = service.closeAccountById("a1");

        assertEquals(AccountStatus.CLOSED, dto.getStatus());
        verify(accountRepository).findByAccountId("a1");
        verify(accountRepository).save(existing);
        verifyNoInteractions(balanceService);
    }

    @Test
    void closeAccountByUserId_setsClosed() {
        Account existing = account("a1", "u1", Role.FAN, AccountStatus.ACTIVE, BigDecimal.ZERO);
        when(accountRepository.findByUserId("u1")).thenReturn(Optional.of(existing));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        AccountResponseDTO dto = service.closeAccountByUserId("u1");

        assertEquals(AccountStatus.CLOSED, dto.getStatus());
        verify(accountRepository).findByUserId("u1");
        verify(accountRepository).save(existing);
        verifyNoInteractions(balanceService);
    }

    @Test
    void enableAccountById_setsActive() {
        Account existing = account("a1", "u1", Role.FAN, AccountStatus.SUSPENDED, BigDecimal.ZERO);
        when(accountRepository.findByAccountId("a1")).thenReturn(Optional.of(existing));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        AccountResponseDTO dto = service.enableAccountById("a1");

        assertEquals(AccountStatus.ACTIVE, dto.getStatus());
        verify(accountRepository).findByAccountId("a1");
        verify(accountRepository).save(existing);
        verifyNoInteractions(balanceService);
    }

    @Test
    void enableAccountByUserId_setsActive() {
        Account existing = account("a1", "u1", Role.FAN, AccountStatus.SUSPENDED, BigDecimal.ZERO);
        when(accountRepository.findByUserId("u1")).thenReturn(Optional.of(existing));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        AccountResponseDTO dto = service.enableAccountByUserId("u1");

        assertEquals(AccountStatus.ACTIVE, dto.getStatus());
        verify(accountRepository).findByUserId("u1");
        verify(accountRepository).save(existing);
        verifyNoInteractions(balanceService);
    }

    @Test
    void suspendAccountById_setsSuspended() {
        Account existing = account("a1", "u1", Role.FAN, AccountStatus.ACTIVE, BigDecimal.ZERO);
        when(accountRepository.findByAccountId("a1")).thenReturn(Optional.of(existing));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        AccountResponseDTO dto = service.suspendAccountById("a1");

        assertEquals(AccountStatus.SUSPENDED, dto.getStatus());
        verify(accountRepository).findByAccountId("a1");
        verify(accountRepository).save(existing);
        verifyNoInteractions(balanceService);
    }

    @Test
    void suspendAccountByUserId_setsSuspended() {
        Account existing = account("a1", "u1", Role.FAN, AccountStatus.ACTIVE, BigDecimal.ZERO);
        when(accountRepository.findByUserId("u1")).thenReturn(Optional.of(existing));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        AccountResponseDTO dto = service.suspendAccountByUserId("u1");

        assertEquals(AccountStatus.SUSPENDED, dto.getStatus());
        verify(accountRepository).findByUserId("u1");
        verify(accountRepository).save(existing);
        verifyNoInteractions(balanceService);
    }
}
