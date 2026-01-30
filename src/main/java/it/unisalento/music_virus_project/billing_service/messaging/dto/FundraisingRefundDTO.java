package it.unisalento.music_virus_project.billing_service.messaging.dto;

public class FundraisingRefundDTO {
    private String fundraisingId;
    private String artistId;

    public FundraisingRefundDTO() {}

    public FundraisingRefundDTO(String fundraisingId, String artistId) {
        this.fundraisingId = fundraisingId;
        this.artistId = artistId;
    }

    public String getFundraisingId() {
        return fundraisingId;
    }
    public void setFundraisingId(String fundraisingId) {
        this.fundraisingId = fundraisingId;
    }
    public String getArtistId() {
        return artistId;
    }
    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

}

