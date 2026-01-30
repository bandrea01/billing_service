package it.unisalento.music_virus_project.billing_service.service;

import it.unisalento.music_virus_project.billing_service.domain.entity.Account;

import java.math.BigDecimal;

public interface IAccountBalanceService {
    Account debitByUserId(String userId, BigDecimal amount);
    Account creditByUserId(String userId, BigDecimal amount);
}
