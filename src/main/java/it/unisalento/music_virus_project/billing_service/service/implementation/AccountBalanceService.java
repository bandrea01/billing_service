package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.Account;
import it.unisalento.music_virus_project.billing_service.exceptions.InsufficentBalanceException;
import it.unisalento.music_virus_project.billing_service.exceptions.NotFoundException;
import it.unisalento.music_virus_project.billing_service.repositories.IAccountRepository;
import it.unisalento.music_virus_project.billing_service.service.IAccountBalanceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class AccountBalanceService implements IAccountBalanceService {

    private final IAccountRepository accountRepository;

    public AccountBalanceService(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account debitByUserId(String userId, BigDecimal amount) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Account non trovato per userId: " + userId));

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Errore: l'importo deve essere maggiore di 0");
        }
        BigDecimal balance = account.getBalance();
        System.out.println("balance=" + balance.toPlainString() + " amount=" + amount.toPlainString() + " compareTo=" + balance.compareTo(amount));

        System.out.println("DEBIT CALLED userId=" + userId + " amount=" + amount);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficentBalanceException("Saldo insufficiente per l'addebito richiesto");
        }

        account.setBalance(account.getBalance().subtract(amount));
        account.setLastUpdatedAt(Instant.now());

        return accountRepository.save(account);
    }

    @Override
    public Account creditByUserId(String userId, BigDecimal amount) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Account non trovato per userId: " + userId));

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Errore: l'importo deve essere maggiore di 0");
        }

        account.setBalance(account.getBalance().add(amount));
        account.setLastUpdatedAt(Instant.now());

        return accountRepository.save(account);
    }
}
