package br.com.gmob.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gmob.jwt")
public record JwtProperties(
        String secret,
        long expiresInSeconds
) {
}
