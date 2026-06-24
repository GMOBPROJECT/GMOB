package br.com.gmob.auth.infrastructure.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {

    private static final String JWT_KEY_PREFIX = "jwt:";
    private static final String LOGIN_ATTEMPTS_KEY_PREFIX = "login_attempts:";

    private final StringRedisTemplate redisTemplate;

    public RedisCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setToken(Long userId, String token, long ttlSeconds) {
        redisTemplate.opsForValue().set(jwtKey(userId), token, Duration.ofSeconds(ttlSeconds));
    }

    public String getToken(Long userId) {
        return redisTemplate.opsForValue().get(jwtKey(userId));
    }

    public void removeToken(Long userId) {
        redisTemplate.delete(jwtKey(userId));
    }

    public void setLoginAttempts(String email, int attempts, long ttlSeconds) {
        redisTemplate.opsForValue().set(
                loginAttemptsKey(email),
                String.valueOf(attempts),
                ttlSeconds,
                TimeUnit.SECONDS
        );
    }

    public int getLoginAttempts(String email) {
        String attempts = redisTemplate.opsForValue().get(loginAttemptsKey(email));
        if (attempts == null || attempts.isBlank()) {
            return 0;
        }
        return Integer.parseInt(attempts);
    }

    public void removeLoginAttempts(String email) {
        redisTemplate.delete(loginAttemptsKey(email));
    }

    private String jwtKey(Long userId) {
        return JWT_KEY_PREFIX + userId;
    }

    private String loginAttemptsKey(String email) {
        return LOGIN_ATTEMPTS_KEY_PREFIX + email;
    }
}
