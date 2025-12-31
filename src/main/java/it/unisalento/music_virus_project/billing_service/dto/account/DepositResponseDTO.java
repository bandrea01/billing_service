package it.unisalento.music_virus_project.billing_service.dto.account;

import java.math.BigDecimal;
import java.time.Instant;

public class DepositResponseDTO {

    private String userId;
    private String accountId;
    private BigDecimal depositAmount;
    private BigDecimal actualBalance;
    private Instant timestamp;

    public DepositResponseDTO() {}

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public BigDecimal getDepositAmount() {
        return depositAmount;
    }
    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }
    public BigDecimal getActualBalance() {
        return actualBalance;
    }
    public void setActualBalance(BigDecimal actualBalance) {
        this.actualBalance = actualBalance;
    }
    public Instant getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
