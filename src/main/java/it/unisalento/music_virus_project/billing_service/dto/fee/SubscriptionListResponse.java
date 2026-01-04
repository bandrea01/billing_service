package it.unisalento.music_virus_project.billing_service.dto.fee;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionListResponse {
    private List<SubscriptionResponseDTO> subscriptions;

    public SubscriptionListResponse(List<SubscriptionResponseDTO> fees) {
        this.subscriptions = fees;
    }
    public SubscriptionListResponse() {
        this.subscriptions = new ArrayList<>();
    }

    public List<SubscriptionResponseDTO> getSubscriptions() {
        return subscriptions;
    }
    public void setSubscriptions(List<SubscriptionResponseDTO> subscriptions) {
        this.subscriptions = subscriptions;
    }
    public void addFee(SubscriptionResponseDTO fee) {
        this.subscriptions.add(fee);
    }
}
