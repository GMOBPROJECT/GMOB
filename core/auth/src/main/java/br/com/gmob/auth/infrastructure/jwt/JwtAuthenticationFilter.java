package br.com.gmob.auth.infrastructure.jwt;

import br.com.gmob.auth.infrastructure.redis.RedisCacheService;
import br.com.gmob.corretor.application.port.CorretorAuthPort;
import br.com.gmob.corretor.domain.model.Corretor;
import br.com.gmob.infra.security.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisCacheService redisCacheService;
    private final CorretorAuthPort corretorAuthPort;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            RedisCacheService redisCacheService,
            CorretorAuthPort corretorAuthPort
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisCacheService = redisCacheService;
        this.corretorAuthPort = corretorAuthPort;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());

            try {
                Claims claims = jwtTokenProvider.validateAndGetClaims(token);
                Long userId = Long.parseLong(claims.getSubject());
                String cachedToken = redisCacheService.getToken(userId);

                if (cachedToken != null && cachedToken.equals(token)) {
                    corretorAuthPort.findByIdForAuth(userId).ifPresent(corretor -> {
                        AuthenticatedUser authenticatedUser = toAuthenticatedUser(corretor);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                authenticatedUser,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + authenticatedUser.perfil().name()))
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
                }
            } catch (Exception ignored) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private AuthenticatedUser toAuthenticatedUser(Corretor corretor) {
        return new AuthenticatedUser(
                corretor.id(),
                corretor.nomeCompleto(),
                corretor.email(),
                corretor.telefone(),
                corretor.cpf(),
                corretor.perfil(),
                corretor.dataCadastro()
        );
    }
}
