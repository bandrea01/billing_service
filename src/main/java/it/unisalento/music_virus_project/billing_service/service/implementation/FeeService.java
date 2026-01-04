package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.fee.Tax;
import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.entity.fee.Subscription;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;
import it.unisalento.music_virus_project.billing_service.dto.fee.*;
import it.unisalento.music_virus_project.billing_service.exceptions.AlreadyExistingFeePlanException;
import it.unisalento.music_virus_project.billing_service.exceptions.NotFoundException;
import it.unisalento.music_virus_project.billing_service.repositories.ITaxRepository;
import it.unisalento.music_virus_project.billing_service.repositories.ISubscriptionRepository;
import it.unisalento.music_virus_project.billing_service.service.IFeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeeService implements IFeeService {

    private final ISubscriptionRepository subscriptionRepository;
    private final ITaxRepository taxRepository;

    public FeeService(ISubscriptionRepository feeRepository, ITaxRepository taxRepository) {
        this.subscriptionRepository = feeRepository;
        this.taxRepository = taxRepository;
    }

    @Override
    public SubscriptionListResponse getSubscriptionList() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return mapSubscriptionsToDTOlist(subscriptions);
    }

    @Override
    public TaxListResponseDTO getTaxList() {
        List<Tax> fees = taxRepository.findAll();
        return mapTaxesToDTOlist(fees);
    }

    @Override
    public SubscriptionListResponse getArtistFees() {
        List<Subscription> fees = subscriptionRepository.findSubscriptionByIsApplicatedToContains(Role.ARTIST);
        return mapSubscriptionsToDTOlist(fees);
    }

    @Override
    public SubscriptionListResponse getVenuesFees() {
        List<Subscription> fees = subscriptionRepository.findSubscriptionByIsApplicatedToContains(Role.VENUE);
        return mapSubscriptionsToDTOlist(fees);
    }

    @Override
    public SubscriptionListResponse getFansFees() {
        List<Subscription> fees = subscriptionRepository.findSubscriptionByIsApplicatedToContains(Role.FAN);
        return mapSubscriptionsToDTOlist(fees);
    }

    @Override
    @Transactional
    public SubscriptionResponseDTO createSubscription(SubscriptionCreateRequestDTO subscriptionCreateRequestDTO) {
        // Any user type should have only one fee plan
        List<Subscription> existingSubscription = subscriptionRepository.findSubscriptionByIsApplicatedToContains(subscriptionCreateRequestDTO.getIsApplicatedTo().iterator().next());
        if (existingSubscription != null) {
            throw new AlreadyExistingFeePlanException("Errore: esiste già un piano tariffario per uno dei ruoli specificati.");
        }

        Subscription subscription = new Subscription();
        subscription.setIsApplicatedTo(new ArrayList<>(subscriptionCreateRequestDTO.getIsApplicatedTo()));
        subscription.setAmount(subscriptionCreateRequestDTO.getAmount());
        subscription.setFeePeriod(subscriptionCreateRequestDTO.getFeePeriod());
        subscription.setActiveSince(subscriptionCreateRequestDTO.getActiveSince());

        subscription = subscriptionRepository.save(subscription);

        return mapToDTO(subscription);
    }

    @Override
    @Transactional
    public TaxResponseDTO createTax(TaxCreateRequestDTO taxCreateRequestDTO) {
        Tax tax = new Tax();
        tax.setTaxName(taxCreateRequestDTO.getTaxName());
        tax.setPercentageOnTotal(taxCreateRequestDTO.getEventTaxPercentage());
        tax.setActiveSince(taxCreateRequestDTO.getActiveSince());

        tax = taxRepository.save(tax);

        return mapToDTO(tax);
    }

    @Override
    @Transactional
    public SubscriptionResponseDTO updateSubscription(String feePlanId, SubscriptionUpdateRequestDTO subscriptionUpdateRequestDTO) {
        Subscription subscription = subscriptionRepository.findSubscriptionByFeePlanId(feePlanId);
        if (subscription == null) {
            throw new NotFoundException("Errore: piano tariffario non trovato.");
        }
        if (subscriptionUpdateRequestDTO.getIsApplicatedTo() != null) {
            subscription.setIsApplicatedTo(new ArrayList<>(subscriptionUpdateRequestDTO.getIsApplicatedTo()));
        }
        if (subscriptionUpdateRequestDTO.getAmount() != null) {
            subscription.setAmount(subscriptionUpdateRequestDTO.getAmount());
        }
        if (subscriptionUpdateRequestDTO.getFeePeriod() != null) {
            subscription.setFeePeriod(subscriptionUpdateRequestDTO.getFeePeriod());
        }
        if (subscriptionUpdateRequestDTO.getActiveSince() != null) {
            subscription.setActiveSince(subscriptionUpdateRequestDTO.getActiveSince());
        }
        subscriptionRepository.save(subscription);
        return mapToDTO(subscription);
    }

    @Override
    @Transactional
    public TaxResponseDTO updateTax(String feePlanId, TaxUpdateRequestDTO taxUpdateRequestDTO) {
        Tax eventTax = taxRepository.findTaxByFeePlanId(feePlanId);
        if (eventTax == null) {
            throw new NotFoundException("Errore: tassazione non trovata.");
        }
        if (taxUpdateRequestDTO.getEventTaxPercentage() != null) {
            eventTax.setPercentageOnTotal(taxUpdateRequestDTO.getEventTaxPercentage());
        }
        if(taxUpdateRequestDTO.getTaxName() != null) {
            Tax existingTax = taxRepository.findTaxByTaxName(taxUpdateRequestDTO.getTaxName());
            if (existingTax != null && !existingTax.getFeePlanId().equals(feePlanId)) {
                throw new AlreadyExistingFeePlanException("Errore: esiste già una tassazione con questo nome");
            }
            eventTax.setTaxName(taxUpdateRequestDTO.getTaxName());
        }
        taxRepository.save(eventTax);
        return mapToDTO(eventTax);
    }

    //utils
    private SubscriptionResponseDTO mapToDTO(Subscription subscription) {
        if (subscription == null) return null;

        SubscriptionResponseDTO dto = new SubscriptionResponseDTO();
        dto.setFeeType(FeeType.SUBSCRIPTION);
        dto.setFeePlanId(subscription.getFeePlanId());
        dto.setIsApplicatedTo(
                subscription.getIsApplicatedTo() == null
                        ? new ArrayList<>()
                        : new ArrayList<>(subscription.getIsApplicatedTo())
        );
        dto.setAmount(subscription.getAmount());
        dto.setFeePeriod(subscription.getFeePeriod());
        dto.setActiveSince(subscription.getActiveSince());
        return dto;
    }
    private TaxResponseDTO mapToDTO(Tax eventTax) {
        if (eventTax == null) return null;

        TaxResponseDTO dto = new TaxResponseDTO();
        dto.setFeeType(FeeType.TAX);
        dto.setFeePlanId(eventTax.getFeePlanId());
        dto.setTaxName(eventTax.getTaxName().toString());
        dto.setEventTaxPercentage(eventTax.getPercentageOnTotal());
        dto.setActiveSince(eventTax.getActiveSince());
        return dto;
    }
    private SubscriptionListResponse mapSubscriptionsToDTOlist(List<Subscription> subscriptions) {
        SubscriptionListResponse dtoList = new SubscriptionListResponse();
        List<SubscriptionResponseDTO> dtoItems = new ArrayList<>();
        for (Subscription subscription : subscriptions) {
            dtoItems.add(mapToDTO(subscription));
        }
        dtoList.setSubscriptions(dtoItems);
        return dtoList;
    }
    private TaxListResponseDTO mapTaxesToDTOlist(List<Tax> eventTaxes) {
        TaxListResponseDTO dtoList = new TaxListResponseDTO();
        List<TaxResponseDTO> dtoItems = new ArrayList<>();
        for (Tax eventTax : eventTaxes) {
            dtoItems.add(mapToDTO(eventTax));
        }
        dtoList.setTaxes(dtoItems);
        return dtoList;
    }



}
