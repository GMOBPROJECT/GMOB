package br.com.gmob.corretor.application;

import br.com.gmob.corretor.api.dto.CorretorResponse;
import br.com.gmob.corretor.api.mapper.CorretorMapper;
import br.com.gmob.corretor.domain.port.CorretorRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class FindCorretorUseCase {

    private final CorretorRepositoryPort corretorRepository;

    public FindCorretorUseCase(CorretorRepositoryPort corretorRepository) {
        this.corretorRepository = corretorRepository;
    }

    public CorretorResponse execute(Long id, AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.ADMINISTRADOR && !currentUser.corretorId().equals(id)) {
            throw new ForbiddenException("Você só pode visualizar seu próprio perfil");
        }

        return corretorRepository.findById(id)
                .map(CorretorMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Corretor não encontrado"));
    }
}
