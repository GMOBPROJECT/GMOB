package br.com.gmob.auth.api;

import br.com.gmob.auth.api.dto.LoginRequest;
import br.com.gmob.auth.api.dto.LoginResponse;
import br.com.gmob.auth.api.dto.RegisterRequest;
import br.com.gmob.auth.api.dto.RegisterResponse;
import br.com.gmob.auth.application.GetProfileUseCase;
import br.com.gmob.auth.application.LoginUseCase;
import br.com.gmob.auth.application.LogoutUseCase;
import br.com.gmob.auth.application.RegisterUseCase;
import br.com.gmob.corretor.api.dto.CorretorResponse;
import br.com.gmob.infra.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;
    private final GetProfileUseCase getProfileUseCase;

    public AuthController(
            RegisterUseCase registerUseCase,
            LoginUseCase loginUseCase,
            LogoutUseCase logoutUseCase,
            GetProfileUseCase getProfileUseCase
    ) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
        this.logoutUseCase = logoutUseCase;
        this.getProfileUseCase = getProfileUseCase;
    }

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return registerUseCase.execute(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return loginUseCase.execute(request);
    }

    @PostMapping("/logout")
    public Map<String, String> logout() {
        return logoutUseCase.execute(SecurityUtils.getCurrentUser().corretorId());
    }

    @GetMapping("/profile")
    public CorretorResponse getProfile() {
        return getProfileUseCase.execute(SecurityUtils.getCurrentUser().corretorId());
    }
}
