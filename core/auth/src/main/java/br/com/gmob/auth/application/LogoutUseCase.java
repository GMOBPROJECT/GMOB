package br.com.gmob.auth.application;

import br.com.gmob.auth.infrastructure.redis.RedisCacheService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LogoutUseCase {

    private final RedisCacheService redisCacheService;

    public LogoutUseCase(RedisCacheService redisCacheService) {
        this.redisCacheService = redisCacheService;
    }

    public Map<String, String> execute(Long corretorId) {
        redisCacheService.removeToken(corretorId);
        return Map.of("message", "Logout realizado com sucesso");
    }
}
