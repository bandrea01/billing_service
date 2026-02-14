package it.unisalento.music_virus_project.billing_service.dto.refund;

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
