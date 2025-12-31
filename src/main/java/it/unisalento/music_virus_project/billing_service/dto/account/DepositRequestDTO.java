package it.unisalento.music_virus_project.billing_service.dto.account;

import java.math.BigDecimal;

public class DepositRequestDTO {

    private String userId;
    private BigDecimal amount;

    public DepositRequestDTO() {}

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
