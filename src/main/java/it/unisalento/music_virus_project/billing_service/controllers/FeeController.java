package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.dto.fee.*;
import it.unisalento.music_virus_project.billing_service.service.IFeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/fee")
@Validated
public class FeeController {

    private final IFeeService feeService;

    public FeeController(IFeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<SubscriptionListResponse> getSubscriptionList() {
        var response = feeService.getSubscriptionList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subscriptions/artists")
    public ResponseEntity<SubscriptionListResponse> getArtistsFees() {
        var response = feeService.getArtistFees();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subscriptions/venues")
    public ResponseEntity<SubscriptionListResponse> getVenuesFees() {
        var response = feeService.getVenuesFees();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subscriptions/fans")
    public ResponseEntity<SubscriptionListResponse> getFansFees() {
        var response = feeService.getFansFees();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/taxes")
    public ResponseEntity<TaxListResponseDTO> getTaxList() {
        var response = feeService.getTaxList();
        return ResponseEntity.ok(response);
    }

}
