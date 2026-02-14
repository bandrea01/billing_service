package it.unisalento.music_virus_project.billing_service.dto.contribution;

import java.math.BigDecimal;

public class TopContributorResponseDTO {
    private String userId;
    private BigDecimal amount;
    private boolean isAnonymous;

    public TopContributorResponseDTO(){}

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public boolean isAnonymous() {
        return isAnonymous;
    }
    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }
}
