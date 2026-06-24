package br.com.gmob.auth.application;

import br.com.gmob.auth.api.dto.LoginRequest;
import br.com.gmob.auth.api.dto.LoginResponse;
import br.com.gmob.auth.infrastructure.jwt.JwtTokenProvider;
import br.com.gmob.auth.infrastructure.redis.RedisCacheService;
import br.com.gmob.corretor.api.mapper.CorretorMapper;
import br.com.gmob.corretor.application.port.CorretorAuthPort;
import br.com.gmob.corretor.domain.model.Corretor;
import br.com.gmob.infra.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoginUseCase {

    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final long LOGIN_ATTEMPTS_TTL_SECONDS = 900;
    private static final long TOKEN_TTL_SECONDS = 7 * 24 * 3600;

    private final CorretorAuthPort corretorAuthPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisCacheService redisCacheService;

    public LoginUseCase(
            CorretorAuthPort corretorAuthPort,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            RedisCacheService redisCacheService
    ) {
        this.corretorAuthPort = corretorAuthPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisCacheService = redisCacheService;
    }

    public LoginResponse execute(LoginRequest request) {
        int attempts = redisCacheService.getLoginAttempts(request.email());
        if (attempts >= MAX_LOGIN_ATTEMPTS) {
            throw new BusinessException("Muitas tentativas de login. Tente novamente em 15 minutos.");
        }

        Corretor corretor = corretorAuthPort.findByEmailWithPassword(request.email()).orElse(null);

        if (corretor == null || corretor.senha().isEmpty()
                || !passwordEncoder.matches(request.senha(), corretor.senha().get())) {
            redisCacheService.setLoginAttempts(request.email(), attempts + 1, LOGIN_ATTEMPTS_TTL_SECONDS);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        redisCacheService.removeLoginAttempts(request.email());

        String token = jwtTokenProvider.generateToken(corretor.email(), corretor.id(), corretor.perfil());
        redisCacheService.setToken(corretor.id(), token, TOKEN_TTL_SECONDS);

        return new LoginResponse(token, CorretorMapper.toResponse(corretor.withoutSenha()));
    }
}
