package it.unisalento.music_virus_project.billing_service.messaging.dto;

import java.math.BigDecimal;

public class EventCreationDTO {
    private String artistId;
    private BigDecimal amount;

    public String getArtistId() {
        return artistId;
    }
    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
