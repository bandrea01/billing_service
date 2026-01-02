package it.unisalento.music_virus_project.billing_service.dto.fee;

import java.util.ArrayList;
import java.util.List;

public class FeeListResponseDTO {
    private List<FeeResponseDTO> fees;

    public FeeListResponseDTO(List<FeeResponseDTO> fees) {
        this.fees = fees;
    }
    public FeeListResponseDTO() {
        this.fees = new ArrayList<>();
    }

    public List<FeeResponseDTO> getFees() {
        return fees;
    }
    public void setFees(List<FeeResponseDTO> fees) {
        this.fees = fees;
    }
    public void addFee(FeeResponseDTO fee) {
        this.fees.add(fee);
    }
}
