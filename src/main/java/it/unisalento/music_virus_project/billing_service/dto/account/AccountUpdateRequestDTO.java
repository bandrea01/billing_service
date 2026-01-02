package it.unisalento.music_virus_project.billing_service.dto.account;

import it.unisalento.music_virus_project.billing_service.domain.enums.AccountStatus;

import java.math.BigDecimal;

public class AccountUpdateRequestDTO {
    private BigDecimal accountBalance;
    private AccountStatus accountStatus;

    public AccountUpdateRequestDTO() {
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }
    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }
    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
}
