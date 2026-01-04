package it.unisalento.music_virus_project.billing_service.dto.account;

import it.unisalento.music_virus_project.billing_service.domain.enums.AccountStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class AccountResponseDTO {

    private String accountId;
    private String userId;
    private BigDecimal balance;
    private AccountStatus status;
    private Instant lastUpdate;

    public AccountResponseDTO() {}

    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public AccountStatus getStatus() {
        return status;
    }
    public void setStatus(AccountStatus status) {
        this.status = status;
    }
    public Instant getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
