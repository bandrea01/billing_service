package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.dto.transaction.TransactionListResponseDTO;
import it.unisalento.music_virus_project.billing_service.service.ITransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/billing/transactions")
@Validated
public class TransactionController {

    private final ITransactionService transactionService;

    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/all")
    public ResponseEntity<TransactionListResponseDTO> getAllTransactions(@AuthenticationPrincipal Jwt principal) {
        String userId = principal.getClaimAsString("userId");
        var response = transactionService.getAllTransactionsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/last-10")
    public ResponseEntity<TransactionListResponseDTO> getLast10Transactions(@AuthenticationPrincipal Jwt principal) {
        String userId = principal.getClaimAsString("userId");
        var response = transactionService.getLast10TransactionsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/last-10/incoming")
    public ResponseEntity<TransactionListResponseDTO> getLast10IncomingTransactions(@AuthenticationPrincipal Jwt principal) {
        String userId = principal.getClaimAsString("userId");
        var response = transactionService.getLast10IncomingTransactionsByUserId(userId);
        return ResponseEntity.ok(response);
    }

}
