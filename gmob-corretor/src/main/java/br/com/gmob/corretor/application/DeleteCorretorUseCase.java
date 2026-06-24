package br.com.gmob.corretor.application;

import br.com.gmob.corretor.domain.port.CorretorRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DeleteCorretorUseCase {

    private final CorretorRepositoryPort corretorRepository;

    public DeleteCorretorUseCase(CorretorRepositoryPort corretorRepository) {
        this.corretorRepository = corretorRepository;
    }

    public Map<String, String> execute(Long id, AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Apenas administradores podem remover corretores");
        }

        corretorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Corretor não encontrado"));

        if (currentUser.corretorId().equals(id)) {
            throw new ForbiddenException("Você não pode remover seu próprio usuário");
        }

        corretorRepository.delete(id);

        return Map.of("message", "Corretor removido com sucesso");
    }
}
