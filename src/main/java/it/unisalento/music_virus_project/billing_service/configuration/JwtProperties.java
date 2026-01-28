package it.unisalento.music_virus_project.billing_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @Value("${jwt.secret.b64}")
    private String secretB64;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    public String getSecretB64() { return secretB64; }
    public void setSecretB64(String secretB64) { this.secretB64 = secretB64; }

    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }

    public long getExpirationMs() { return expirationMs; }
    public void setExpirationMs(long expirationMs) { this.expirationMs = expirationMs; }
}