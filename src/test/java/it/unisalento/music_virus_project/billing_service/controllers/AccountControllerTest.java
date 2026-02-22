package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.enums.AccountStatus;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.account.AccountUpdateRequestDTO;
import it.unisalento.music_virus_project.billing_service.service.IAccountService;
import it.unisalento.music_virus_project.billing_service.service.implementation.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import(AccountControllerTest.TestSecurityConfig.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAccountService accountService;
    @MockBean
    private TransactionService transactionService;

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                    .build();
        }

        @Bean
        JwtDecoder jwtDecoder() {
            return token -> Jwt.withTokenValue(token)
                    .header("alg", "none")
                    .claim("userId", "user1")
                    .claim("role", "ARTIST")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(3600))
                    .build();
        }
    }

    private AccountResponseDTO sampleAccount() {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountId("acc1");
        dto.setUserId("user1");
        dto.setBalance(new BigDecimal("100.00"));
        dto.setStatus(AccountStatus.ACTIVE);
        return dto;
    }

    @Test
    void getPersonalAccount_returnsOk() throws Exception {
        when(accountService.getAccountByUserId("user1")).thenReturn(sampleAccount());

        mockMvc.perform(get("/api/billing/account")
                        .with(jwt().jwt(j -> j.claim("userId", "user1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("acc1"))
                .andExpect(jsonPath("$.userId").value("user1"));
    }

    @Test
    void createAccount_returnsOk() throws Exception {
        when(accountService.createAccount("user1", Role.ARTIST)).thenReturn(sampleAccount());

        mockMvc.perform(post("/api/billing/account")
                        .with(jwt().jwt(j -> j
                                .claim("userId", "user1")
                                .claim("role", "ROLE_ARTIST")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("acc1"));
    }

    @Test
    void updateAccount_returnsOk() throws Exception {
        String json = """
                { "accountStatus": "SUSPENDED" }
                """;

        when(accountService.updateAccount(
                ArgumentMatchers.eq("user1"),
                ArgumentMatchers.any(AccountUpdateRequestDTO.class)
        )).thenReturn(sampleAccount());

        mockMvc.perform(patch("/api/billing/account")
                        .with(jwt().jwt(j -> j.claim("userId", "user1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("acc1"));
    }

    @Test
    void deposit_returnsOk() throws Exception {
        String json = """
                { "amount": 50 }
                """;

        when(accountService.depositByUserId("user1", new BigDecimal("50")))
                .thenReturn(sampleAccount());

        mockMvc.perform(patch("/api/billing/account/deposit")
                        .with(jwt().jwt(j -> j.claim("userId", "user1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("acc1"));
    }
}
