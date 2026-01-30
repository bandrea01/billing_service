package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountUpdateRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.DepositRequestDTO;
import it.unisalento.music_virus_project.billing_service.service.IAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/account")
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
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
        String roleStr = principal.getClaimAsString("role");
        Role role = Role.valueOf(roleStr);
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
}
