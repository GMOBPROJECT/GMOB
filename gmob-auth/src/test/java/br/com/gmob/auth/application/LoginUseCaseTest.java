package br.com.gmob.auth.application;

import br.com.gmob.auth.api.dto.LoginRequest;
import br.com.gmob.auth.infrastructure.jwt.JwtTokenProvider;
import br.com.gmob.auth.infrastructure.redis.RedisCacheService;
import br.com.gmob.corretor.application.port.CorretorAuthPort;
import br.com.gmob.corretor.domain.model.Corretor;
import br.com.gmob.infra.domain.enums.Perfil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private CorretorAuthPort corretorAuthPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RedisCacheService redisCacheService;

    private LoginUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new LoginUseCase(corretorAuthPort, passwordEncoder, jwtTokenProvider, redisCacheService);
    }

    @Test
    void deveAutenticarComCredenciaisValidas() {
        Corretor corretor = new Corretor(
                1L,
                "Corretor",
                "user@test.com",
                "(82) 99999-9999",
                "111.111.111-11",
                Optional.of("hash"),
                Perfil.CORRETOR,
                Instant.now()
        );

        LoginRequest request = new LoginRequest("user@test.com", "senha123");

        when(redisCacheService.getLoginAttempts("user@test.com")).thenReturn(0);
        when(corretorAuthPort.findByEmailWithPassword("user@test.com")).thenReturn(Optional.of(corretor));
        when(passwordEncoder.matches("senha123", "hash")).thenReturn(true);
        when(jwtTokenProvider.generateToken("user@test.com", 1L, Perfil.CORRETOR)).thenReturn("jwt-token");

        var response = useCase.execute(request);

        assertEquals("jwt-token", response.accessToken());
        assertNotNull(response.user());
        verify(redisCacheService).removeLoginAttempts("user@test.com");
        verify(redisCacheService).setToken(1L, "jwt-token", 7 * 24 * 3600);
    }

    @Test
    void deveRejeitarCredenciaisInvalidas() {
        LoginRequest request = new LoginRequest("user@test.com", "wrong");

        when(redisCacheService.getLoginAttempts("user@test.com")).thenReturn(0);
        when(corretorAuthPort.findByEmailWithPassword("user@test.com")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> useCase.execute(request));
    }
}
