package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.domain.entity.Account;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeCreateRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeUpdateRequestDTO;
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
    @PostMapping("/fee")
    public ResponseEntity<FeeResponseDTO> createFee(@RequestBody FeeCreateRequestDTO feeCreateRequestDTO) {
        var response = feeService.createFee(feeCreateRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/fee/{feePlanId}")
    public ResponseEntity<FeeResponseDTO> updateFee(@PathVariable String feePlanId, @RequestBody FeeUpdateRequestDTO feeUpdateRequestDTO) {
        var response = feeService.updateFee(feePlanId, feeUpdateRequestDTO);
        return ResponseEntity.ok(response);
    }

}
