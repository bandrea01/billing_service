package it.unisalento.music_virus_project.billing_service.domain.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.Instant;

public class Account {

    @Id
    private String accountId;

    @Indexed
    private String userId;

    private BigDecimal balance;

    @CreatedDate
    private Instant createdAt;
    private Instant lastUpdate;

    public Account(String userId) {
        this.userId = userId;
        this.balance = BigDecimal.ZERO;
        this.createdAt = Instant.now();
        this.lastUpdate = Instant.now();
    }
    public Account(String userId, BigDecimal initialBalance) {
        this.userId = userId;
        this.balance = initialBalance;
        this.createdAt = Instant.now();
        this.lastUpdate = Instant.now();
    }
    public Account() {
        this.userId = "";
        this.balance = BigDecimal.ZERO;
        this.createdAt = Instant.now();
        this.lastUpdate = Instant.now();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.lastUpdate = Instant.now();
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.lastUpdate = Instant.now();
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        this.lastUpdate = Instant.now();
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.lastUpdate = Instant.now();
        this.balance = balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.lastUpdate = Instant.now();
        this.createdAt = createdAt;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
