package it.unisalento.music_virus_project.billing_service.service;

import it.unisalento.music_virus_project.billing_service.dto.fee.*;

public interface IFeeService {
    SubscriptionListResponse getSubscriptionList();
    SubscriptionListResponse getArtistSubscription();
    SubscriptionListResponse getVenuesSubscription();
    SubscriptionListResponse getFansSubscription();
    SubscriptionResponseDTO createSubscription(SubscriptionCreateRequestDTO subscriptionCreateRequestDTO);
    SubscriptionResponseDTO updateSubscription(String feePlanId, SubscriptionUpdateRequestDTO subscriptionUpdateRequestDTO);

    TaxListResponseDTO getTaxList();
    TaxResponseDTO createTax(TaxCreateRequestDTO taxCreateRequestDTO);
    TaxResponseDTO updateTax(String feePlanId, TaxUpdateRequestDTO taxUpdateRequestDTO);
}
