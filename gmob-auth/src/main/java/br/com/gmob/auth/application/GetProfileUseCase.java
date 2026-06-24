package br.com.gmob.auth.application;

import br.com.gmob.corretor.api.dto.CorretorResponse;
import br.com.gmob.corretor.api.mapper.CorretorMapper;
import br.com.gmob.corretor.application.port.CorretorAuthPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GetProfileUseCase {

    private final CorretorAuthPort corretorAuthPort;

    public GetProfileUseCase(CorretorAuthPort corretorAuthPort) {
        this.corretorAuthPort = corretorAuthPort;
    }

    public CorretorResponse execute(Long corretorId) {
        return corretorAuthPort.findByIdForAuth(corretorId)
                .map(CorretorMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));
    }
}
