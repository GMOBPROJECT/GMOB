package br.com.gmob.auth.infrastructure.jwt;

import br.com.gmob.auth.config.JwtProperties;
import br.com.gmob.infra.domain.enums.Perfil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = buildSigningKey(jwtProperties.secret());
    }

    public String generateToken(String email, Long corretorId, Perfil perfil) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtProperties.expiresInSeconds());

        return Jwts.builder()
                .subject(String.valueOf(corretorId))
                .claim("email", email)
                .claim("perfil", perfil.getValue())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(signingKey)
                .compact();
    }

    public Claims validateAndGetClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey buildSigningKey(String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("gmob.jwt.secret não está configurado");
        }

        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException ex) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
