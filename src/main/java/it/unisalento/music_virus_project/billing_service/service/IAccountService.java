package it.unisalento.music_virus_project.billing_service.service;


import it.unisalento.music_virus_project.billing_service.domain.enums.AccountStatus;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountUpdateRequestDTO;

import java.math.BigDecimal;

public interface IAccountService {
    AccountListResponseDTO getAllAccounts();
    AccountResponseDTO getAccountById(String accountId);
    AccountResponseDTO getAccountByUserId(String userId);
    AccountListResponseDTO getAccountsByStatus(AccountStatus status);
    AccountResponseDTO createAccount(String userId);
    AccountResponseDTO depositByAccountId(String accountId, BigDecimal amount);
    AccountResponseDTO updateAccount(String accountId, AccountUpdateRequestDTO accountUpdateRequestDTO);
    AccountResponseDTO disableAccountById(String accountId);
    AccountResponseDTO disableAccountByUserId(String userId);
    AccountResponseDTO enableAccountById(String accountId);
    AccountResponseDTO enableAccountByUserId(String userId);
}