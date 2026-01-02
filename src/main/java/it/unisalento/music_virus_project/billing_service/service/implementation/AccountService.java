package it.unisalento.music_virus_project.billing_service.service.implementation;


import it.unisalento.music_virus_project.billing_service.domain.entity.Account;
import it.unisalento.music_virus_project.billing_service.domain.enums.AccountStatus;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountUpdateRequestDTO;
import it.unisalento.music_virus_project.billing_service.exceptions.NotFoundException;
import it.unisalento.music_virus_project.billing_service.repositories.AccountRepository;
import it.unisalento.music_virus_project.billing_service.service.IAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public AccountListResponseDTO getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return mapToListDTO(accounts);
    }

    @Override
    public AccountResponseDTO getAccountById(String accountId) {
        Account account = accountRepository.findAccountByAccountId(accountId);
        if (account == null) {
            throw new NotFoundException("Errore: Account non trovato!");
        }
        return mapToDTO(account);
    }

    @Override
    public AccountResponseDTO getAccountByUserId(String userId) {
        Account account = accountRepository.findAccountByUserId(userId);
        if (account == null) {
            throw new NotFoundException("Errore: Account non trovato!");
        }
        return mapToDTO(account);
    }

    @Override
    public AccountListResponseDTO getAccountsByStatus(AccountStatus status) {
        List<Account> accounts = accountRepository.findAccountsByStatus(status);
        return mapToListDTO(accounts);
    }

    @Override
    @Transactional
    public AccountResponseDTO createAccount(String userId) {
        Account account = new Account(userId);
        accountRepository.save(account);
        return mapToDTO(account);
    }

    @Override
    @Transactional
    public AccountResponseDTO updateAccount(String accountId, AccountUpdateRequestDTO accountUpdateRequestDTO) {
        Account account = accountRepository.findAccountByAccountId(accountId);
        if(account == null) {
            throw new NotFoundException("Errore: Account non trovato!");
        }
        if(accountUpdateRequestDTO.getAccountStatus() != null) {
            account.setStatus(accountUpdateRequestDTO.getAccountStatus());
        }
        if(accountUpdateRequestDTO.getAccountBalance() != null) {
            account.setBalance(accountUpdateRequestDTO.getAccountBalance());
        }
        accountRepository.save(account);
        return mapToDTO(account);
    }

    @Override
    @Transactional
    public AccountResponseDTO disableAccountById(String accountId) {
        Account account = accountRepository.findAccountByAccountId(accountId);
        if(account == null) {
            throw new NotFoundException("Errore: Account non trovato!");
        }
        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
        return mapToDTO(account);
    }

    @Override
    @Transactional
    public AccountListResponseDTO disableAccountsByUserId(String userId) {
        Account account = accountRepository.findAccountByUserId(userId);
        if(account == null) {
            throw new NotFoundException("Errore: Account non trovato!");
        }
        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
        return getAccountsByStatus(AccountStatus.CLOSED);
    }

    //utils
    private AccountResponseDTO mapToDTO(Account account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountId(account.getAccountId());
        dto.setUserId(account.getUserId());
        dto.setBalance(account.getBalance());
        dto.setStatus(account.getStatus());
        dto.setLastUpdate(account.getLastUpdate());
        return dto;
    }
    private AccountListResponseDTO mapToListDTO(List<Account> accounts) {
        AccountListResponseDTO listDTO = new AccountListResponseDTO();
        for (Account account : accounts) {
            listDTO.addAccount(mapToDTO(account));
        }
        return listDTO;
    }

}


