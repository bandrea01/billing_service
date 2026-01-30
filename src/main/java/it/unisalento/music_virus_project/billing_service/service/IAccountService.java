package it.unisalento.music_virus_project.billing_service.service;


import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.enums.AccountStatus;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountUpdateRequestDTO;

import java.math.BigDecimal;

public interface IAccountService {
    AccountListResponseDTO getAllAccounts();
    AccountResponseDTO getAccountById(String accountId);
    AccountResponseDTO getAccountByUserId(String userId);
    AccountResponseDTO getAdminAccount();
    AccountResponseDTO createAccount(String userId, Role role);
    AccountResponseDTO depositByUserId(String accountId, BigDecimal amount);
    AccountResponseDTO depositOnAdminAccount(BigDecimal amount);
    AccountResponseDTO updateAccount(String userId, AccountUpdateRequestDTO accountUpdateRequestDTO);
    AccountResponseDTO closeAccountById(String accountId);
    AccountResponseDTO closeAccountByUserId(String userId);
    AccountResponseDTO enableAccountById(String accountId);
    AccountResponseDTO enableAccountByUserId(String userId);
    AccountResponseDTO suspendAccountById(String accountId);
    AccountResponseDTO suspendAccountByUserId(String userId);
}