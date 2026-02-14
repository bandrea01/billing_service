package it.unisalento.music_virus_project.billing_service.dto.transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionListResponseDTO {
    private List<TransactionResponseDTO> transactions;

    public TransactionListResponseDTO() {
        this.transactions = new ArrayList<>();
    }
    public TransactionListResponseDTO(List<TransactionResponseDTO> transactions) {
        this.transactions = transactions;
    }

    public List<TransactionResponseDTO> getTransactions() {
        return transactions;
    }
    public void setTransactions(List<TransactionResponseDTO> transactions) {
        this.transactions = transactions;
    }
    public void addTransaction(TransactionResponseDTO transaction) {
        this.transactions.add(transaction);
    }
}
