package it.unisalento.music_virus_project.billing_service.dto.refund;

import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionStatus;
import it.unisalento.music_virus_project.billing_service.domain.enums.ContributionVisibility;

import java.math.BigDecimal;
import java.time.Instant;

public class RefundResponseDTO {

    private String fundraisingId;

    public RefundResponseDTO() {}

    public String getFundraisingId() {
        return fundraisingId;
    }
    public void setFundraisingId(String fundraisingId) {
        this.fundraisingId = fundraisingId;
    }

}
