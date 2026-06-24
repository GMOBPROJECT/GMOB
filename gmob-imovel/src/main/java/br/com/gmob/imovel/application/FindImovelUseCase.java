package br.com.gmob.imovel.application;

import br.com.gmob.imovel.api.dto.ImovelResponse;
import br.com.gmob.imovel.api.mapper.ImovelMapper;
import br.com.gmob.imovel.domain.model.Imovel;
import br.com.gmob.imovel.domain.port.ImovelRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class FindImovelUseCase {

    private final ImovelRepositoryPort imovelRepository;

    public FindImovelUseCase(ImovelRepositoryPort imovelRepository) {
        this.imovelRepository = imovelRepository;
    }

    public ImovelResponse execute(Long id, AuthenticatedUser currentUser) {
        Imovel imovel = imovelRepository.findByIdWithImagens(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imóvel com ID " + id + " não encontrado."));

        validateOwnership(imovel, currentUser);

        return ImovelMapper.toResponse(imovel);
    }

    void validateOwnership(Imovel imovel, AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.ADMINISTRADOR
                && !imovel.corretorId().equals(currentUser.corretorId())) {
            throw new ForbiddenException("Você não tem permissão para acessar este recurso.");
        }
    }
}
