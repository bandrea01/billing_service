package it.unisalento.music_virus_project.billing_service.domain.entity.fee;

import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;
import org.springframework.data.annotation.Id;

import java.time.Instant;

public class FeePlan {

    @Id
    private String feePlanId;

    private FeeType feeType;
    private Instant activeSince;

    public FeePlan(){
        this.activeSince = Instant.now();
    }

    public String getFeePlanId() {
        return feePlanId;
    }
    public void setFeePlanId(String feePlanId) {
        this.feePlanId = feePlanId;
    }
    public FeeType getFeeType() {
        return feeType;
    }
    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }
    public Instant getActiveSince() {
        return activeSince;
    }
    public void setActiveSince(Instant activeSince) {
        this.activeSince = activeSince;
    }

}
