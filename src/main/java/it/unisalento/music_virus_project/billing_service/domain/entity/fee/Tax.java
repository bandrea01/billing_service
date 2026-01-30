package it.unisalento.music_virus_project.billing_service.domain.entity.fee;

import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;
import it.unisalento.music_virus_project.billing_service.domain.enums.TaxEnum;

import java.math.BigDecimal;

public class Tax extends FeePlan {
    private TaxEnum taxName;
    private BigDecimal percentageOnTotal;

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
    public BigDecimal getPercentageOnTotal() {
        return percentageOnTotal;
    }
    public void setPercentageOnTotal(BigDecimal percentageOnTotal) {
        this.percentageOnTotal = percentageOnTotal;
    }
}
