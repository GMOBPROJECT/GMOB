package br.com.gmob.auth.application;

import br.com.gmob.auth.api.dto.RegisterRequest;
import br.com.gmob.auth.api.dto.RegisterResponse;
import br.com.gmob.corretor.api.dto.CreateCorretorRequest;
import br.com.gmob.corretor.application.RegisterCorretorUseCase;
import org.springframework.stereotype.Service;

@Service
public class RegisterUseCase {

    private final RegisterCorretorUseCase registerCorretorUseCase;

    public RegisterUseCase(RegisterCorretorUseCase registerCorretorUseCase) {
        this.registerCorretorUseCase = registerCorretorUseCase;
    }

    public RegisterResponse execute(RegisterRequest request) {
        RegisterCorretorUseCase.RegisterCorretorResult result = registerCorretorUseCase.execute(toCreateRequest(request));
        return new RegisterResponse(result.message(), result.corretor());
    }

    private CreateCorretorRequest toCreateRequest(RegisterRequest request) {
        return new CreateCorretorRequest(
                request.nomeCompleto(),
                request.email(),
                request.telefone(),
                request.cpf(),
                request.senha(),
                request.perfil()
        );
    }
}
