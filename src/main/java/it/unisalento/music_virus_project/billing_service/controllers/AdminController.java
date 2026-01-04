package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.*;
import it.unisalento.music_virus_project.billing_service.service.IAccountService;
import it.unisalento.music_virus_project.billing_service.service.IFeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/admin")
@Validated
public class AdminController {

    private final IAccountService accountService;
    private final IFeeService feeService;

    public AdminController(IAccountService accountService, IFeeService feeService) {
        this.accountService = accountService;
        this.feeService = feeService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/account/disable/{accountId}")
    public ResponseEntity<AccountResponseDTO> disableAccountById(@PathVariable String accountId) {
        var response = accountService.disableAccountById(accountId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/account/disable/user/{userId}")
    public ResponseEntity<AccountResponseDTO> disableAccountByUserId(@PathVariable String userId) {
        var response = accountService.disableAccountByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/account/enable/{accountId}")
    public ResponseEntity<AccountResponseDTO> enableAccountById(@PathVariable String accountId) {
        var response = accountService.enableAccountById(accountId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/account/enable/user/{userId}")
    public ResponseEntity<AccountResponseDTO> enableAccountByUserId(@PathVariable String userId) {
        var response = accountService.enableAccountByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/subscriptions")
    public ResponseEntity<SubscriptionResponseDTO> createSubscription(@RequestBody SubscriptionCreateRequestDTO subscriptionCreateRequestDTO) {
        var response = feeService.createSubscription(subscriptionCreateRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/taxes/{feePlanId}")
    public ResponseEntity<SubscriptionResponseDTO> updateSubscription(@PathVariable String feePlanId, @RequestBody SubscriptionUpdateRequestDTO subscriptionUpdateRequestDTO) {
        var response = feeService.updateSubscription(feePlanId, subscriptionUpdateRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/taxes")
    public ResponseEntity<TaxResponseDTO> createTax(@RequestBody TaxCreateRequestDTO taxCreateRequestDTO) {
        var response = feeService.createTax(taxCreateRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/taxes/{feePlanId}")
    public ResponseEntity<TaxResponseDTO> updateTax(@PathVariable String feePlanId, @RequestBody TaxUpdateRequestDTO taxUpdateRequestDTO) {
        var response = feeService.updateTax(feePlanId, taxUpdateRequestDTO);
        return ResponseEntity.ok(response);
    }

}
