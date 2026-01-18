package it.unisalento.music_virus_project.billing_service.messaging.dto;

import java.math.BigDecimal;

public class ContributionEventDTO {
    private String fundraisingId;
    private BigDecimal amount;

    public ContributionEventDTO() { }

    public ContributionEventDTO(String fundraisingId, BigDecimal amount) {
        this.fundraisingId = fundraisingId;
        this.amount = amount;
    }

    public String getFundraisingId() {
        return fundraisingId;
    }
    public void setFundraisingId(String fundraisingId) {
        this.fundraisingId = fundraisingId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
