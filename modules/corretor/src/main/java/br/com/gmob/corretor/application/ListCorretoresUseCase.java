package br.com.gmob.corretor.application;

import br.com.gmob.corretor.api.dto.CorretorListResponse;
import br.com.gmob.corretor.api.mapper.CorretorMapper;
import br.com.gmob.corretor.domain.port.CorretorRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class ListCorretoresUseCase {

    private final CorretorRepositoryPort corretorRepository;

    public ListCorretoresUseCase(CorretorRepositoryPort corretorRepository) {
        this.corretorRepository = corretorRepository;
    }

    public CorretorListResponse execute(AuthenticatedUser currentUser, int page, int limit) {
        if (currentUser.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Apenas administradores podem listar corretores");
        }

        var corretores = corretorRepository.findAll(page, limit);
        long total = corretorRepository.count();

        return CorretorMapper.toListResponse(corretores, page, limit, total);
    }
}
