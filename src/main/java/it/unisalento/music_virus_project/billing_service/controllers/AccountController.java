package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.domain.enums.AccountStatus;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountUpdateRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.DepositRequestDTO;
import it.unisalento.music_virus_project.billing_service.service.IAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/account")
@Validated
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<AccountListResponseDTO> getAllAccounts() {
        var response = accountService.getAllAccounts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable String accountId) {
        var response = accountService.getAccountById(accountId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<AccountResponseDTO> getAccountByUserId(@PathVariable String userId) {
        var response = accountService.getAccountByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "status")
    public ResponseEntity<AccountListResponseDTO> getAccountsByStatus(@RequestParam String status) {
        var response = accountService.getAccountsByStatus(Enum.valueOf(AccountStatus.class, status));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<AccountResponseDTO> createAccount(@PathVariable String userId) {
        var response = accountService.createAccount(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/deposit/{userId}")
    public ResponseEntity<AccountResponseDTO> depositToAccount(@PathVariable String userId, @RequestBody DepositRequestDTO depositRequest) {
        var response = accountService.depositToAccount(userId, depositRequest.getAmount());
        return ResponseEntity.ok(response);
    }

                                                               @PatchMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> updateAccount(@PathVariable String accountId, @RequestBody AccountUpdateRequestDTO accountUpdateRequest) {
        var response = accountService.updateAccount(accountId, accountUpdateRequest);
        return ResponseEntity.ok(response);
    }

}

