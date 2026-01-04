package it.unisalento.music_virus_project.billing_service.dto.fee;

import java.util.ArrayList;
import java.util.List;

public class TaxListResponseDTO {
    private List<TaxResponseDTO> taxes;

    public TaxListResponseDTO(List<TaxResponseDTO> taxes) {
        this.taxes = taxes;
    }
    public TaxListResponseDTO() {
        this.taxes = new ArrayList<>();
    }

    public List<TaxResponseDTO> getTaxes() {
        return taxes;
    }
    public void setTaxes(List<TaxResponseDTO> taxes) {
        this.taxes = taxes;
    }
    public void addTax(TaxResponseDTO tax) {
        this.taxes.add(tax);
    }
}
