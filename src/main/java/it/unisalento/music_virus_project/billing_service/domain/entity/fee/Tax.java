package it.unisalento.music_virus_project.billing_service.domain.entity.fee;

import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;
import it.unisalento.music_virus_project.billing_service.domain.enums.TaxEnum;

public class Tax extends FeePlan {
    private TaxEnum taxName;
    private Double percentageOnTotal;

    public Tax() {
        super();
        this.setFeeType(FeeType.TAX);
    }

    public TaxEnum getTaxName() {
        return taxName;
    }
    public void setTaxName(TaxEnum taxName) {
        this.taxName = taxName;
    }
    public Double getPercentageOnTotal() {
        return percentageOnTotal;
    }
    public void setPercentageOnTotal(Double percentageOnTotal) {
        this.percentageOnTotal = percentageOnTotal;
    }
}
