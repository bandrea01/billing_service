package it.unisalento.music_virus_project.billing_service.dto.account;

import java.math.BigDecimal;

public class DepositRequestDTO {

    private BigDecimal amount;

    public DepositRequestDTO() {}

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
