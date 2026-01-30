package it.unisalento.music_virus_project.billing_service.domain.entity;

import it.unisalento.music_virus_project.billing_service.domain.enums.AccountStatus;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Document(collection = "accounts")
public class Account {

    @Id
    private String accountId;
    @Indexed(unique = true)
    private String userId;
    @Indexed
    private Role role;
    private BigDecimal balance = BigDecimal.ZERO;
    private AccountStatus status = AccountStatus.ACTIVE;
    @Version
    private Long version;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant lastUpdatedAt;

    public Account() {}

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastUpdatedAt() { return lastUpdatedAt; }

    public Long getVersion() { return version; }
}
