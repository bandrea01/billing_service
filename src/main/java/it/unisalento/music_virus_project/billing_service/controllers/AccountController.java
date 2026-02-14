package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountUpdateRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.DepositRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.transaction.TransactionListResponseDTO;
import it.unisalento.music_virus_project.billing_service.service.IAccountService;
import it.unisalento.music_virus_project.billing_service.service.ITransactionService;
import it.unisalento.music_virus_project.billing_service.service.implementation.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/account")
public class AccountController {

    private final IAccountService accountService;
    private final ITransactionService transactionService;

    public AccountController(IAccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<AccountResponseDTO> getPersonalAccount(@AuthenticationPrincipal Jwt principal) {
        String userId = principal.getClaimAsString("userId");
        var response = accountService.getAccountByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@AuthenticationPrincipal Jwt principal) {
        String userId = principal.getClaimAsString("userId");
        Role role = Role.valueOf(principal.getClaimAsString("role").substring(5));

        System.out.println("Creating account for userId: " + userId + " with role: " + role);
        var response = accountService.createAccount(userId, role);
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @AuthenticationPrincipal Jwt principal,
            @RequestBody AccountUpdateRequestDTO accountUpdateRequest
    ) {
        String userId = principal.getClaimAsString("userId");
        var response = accountService.updateAccount(userId, accountUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/deposit")
    public ResponseEntity<AccountResponseDTO> deposit(@AuthenticationPrincipal Jwt principal,
                                                      @RequestBody DepositRequestDTO depositRequest) {
        String userId = principal.getClaimAsString("userId");
        var response = accountService.depositByUserId(userId, depositRequest.getAmount());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<TransactionListResponseDTO> getAllTransactions(@AuthenticationPrincipal Jwt principal) {
        String userId = principal.getClaimAsString("userId");
        var response = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
