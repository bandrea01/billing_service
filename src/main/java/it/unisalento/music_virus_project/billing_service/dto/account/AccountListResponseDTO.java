package it.unisalento.music_virus_project.billing_service.dto.account;

import java.util.ArrayList;
import java.util.List;

public class AccountListResponseDTO {
    private List<AccountResponseDTO> accounts;

    public AccountListResponseDTO(List<AccountResponseDTO> accounts) {
        this.accounts = accounts;
    }
    public AccountListResponseDTO() {
        this.accounts = new ArrayList<>();
    }

    public List<AccountResponseDTO> getAccounts() {
        return accounts;
    }
    public void setAccounts(List<AccountResponseDTO> accounts) {
        this.accounts = accounts;
    }
    public void addAccount(AccountResponseDTO account) {
        this.accounts.add(account);
    }
}
