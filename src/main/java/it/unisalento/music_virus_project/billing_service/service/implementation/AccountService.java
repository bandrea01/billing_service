package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Account;
import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.enums.AccountStatus;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountUpdateRequestDTO;
import it.unisalento.music_virus_project.billing_service.exceptions.NotFoundException;
import it.unisalento.music_virus_project.billing_service.repositories.IAccountRepository;
import it.unisalento.music_virus_project.billing_service.service.IAccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class AccountService implements IAccountService {

    private final AccountBalanceService balanceService;
    private final IAccountRepository accountRepository;

    public AccountService(IAccountRepository accountRepository, AccountBalanceService balanceService) {
        this.accountRepository = accountRepository;
        this.balanceService = balanceService;
    }

    @Override
    public AccountListResponseDTO getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return mapToListDTO(accounts);
    }

    @Override
    public AccountResponseDTO getAccountById(String accountId) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotFoundException("Account non trovato"));
        return mapToDTO(account);
    }

    @Override
    public AccountResponseDTO getAccountByUserId(String userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Account non trovato"));
        return mapToDTO(account);
    }

    @Override
    public AccountResponseDTO getAdminAccount() {
        Account admin = accountRepository.findFirstByRole(Role.ADMIN)
                .orElseThrow(() -> new NotFoundException("Admin account non trovato"));
        return mapToDTO(admin);
    }

    @Override
    public AccountResponseDTO createAccount(String userId, Role role) {
        Account account = new Account();
        account.setUserId(userId);
        account.setRole(role);
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(Instant.now());
        account.setLastUpdatedAt(Instant.now());

        return mapToDTO(accountRepository.save(account));
    }

    @Override
    public AccountResponseDTO depositByUserId(String userId, BigDecimal amount) {
        Account updated = balanceService.creditByUserId(userId, amount);
        return mapToDTO(updated);
    }

    @Override
    public AccountResponseDTO depositOnAdminAccount(BigDecimal amount) {
        Account admin = accountRepository.findFirstByRole(Role.ADMIN)
                .orElseThrow(() -> new NotFoundException("Admin account non trovato"));
        Account updated = balanceService.creditByUserId(admin.getUserId(), amount);
        return mapToDTO(updated);
    }

    @Override
    public AccountResponseDTO updateAccount(String userId, AccountUpdateRequestDTO dto) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Account non trovato"));

        if (dto.getAccountStatus() != null) {
            account.setStatus(dto.getAccountStatus());
        }

        if (dto.getAccountBalance() != null) {
            account.setBalance(dto.getAccountBalance());
        }

        return mapToDTO(accountRepository.save(account));
    }

    @Override
    public AccountResponseDTO closeAccountById(String accountId) {
        return changeStatusById(accountId, AccountStatus.CLOSED);
    }

    @Override
    public AccountResponseDTO closeAccountByUserId(String userId) {
        return changeStatusByUserId(userId, AccountStatus.CLOSED);
    }

    @Override
    public AccountResponseDTO enableAccountById(String accountId) {
        return changeStatusById(accountId, AccountStatus.ACTIVE);
    }

    @Override
    public AccountResponseDTO enableAccountByUserId(String userId) {
        return changeStatusByUserId(userId, AccountStatus.ACTIVE);
    }

    @Override
    public AccountResponseDTO suspendAccountById(String accountId) {
        return changeStatusById(accountId, AccountStatus.SUSPENDED);
    }

    @Override
    public AccountResponseDTO suspendAccountByUserId(String userId) {
        return changeStatusByUserId(userId, AccountStatus.SUSPENDED);
    }

    private AccountResponseDTO changeStatusById(String accountId, AccountStatus status) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotFoundException("Account non trovato"));
        account.setStatus(status);
        return mapToDTO(accountRepository.save(account));
    }

    private AccountResponseDTO changeStatusByUserId(String userId, AccountStatus status) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Account non trovato"));
        account.setStatus(status);
        return mapToDTO(accountRepository.save(account));
    }

    private AccountResponseDTO mapToDTO(Account account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountId(account.getAccountId());
        dto.setUserId(account.getUserId());
        dto.setBalance(account.getBalance());
        dto.setStatus(account.getStatus());
        dto.setLastUpdatedAt(Instant.now());
        return dto;
    }

    private AccountListResponseDTO mapToListDTO(List<Account> accounts) {
        AccountListResponseDTO listDTO = new AccountListResponseDTO();
        for (Account a : accounts) listDTO.addAccount(mapToDTO(a));
        return listDTO;
    }
}
