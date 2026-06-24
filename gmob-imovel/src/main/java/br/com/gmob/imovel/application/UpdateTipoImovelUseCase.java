package br.com.gmob.imovel.application;

import br.com.gmob.imovel.api.dto.TipoImovelResponse;
import br.com.gmob.imovel.api.dto.UpdateTipoImovelRequest;
import br.com.gmob.imovel.api.mapper.TipoImovelMapper;
import br.com.gmob.imovel.domain.model.TipoImovel;
import br.com.gmob.imovel.domain.port.TipoImovelRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class UpdateTipoImovelUseCase {

    private final TipoImovelRepositoryPort tipoImovelRepository;

    public UpdateTipoImovelUseCase(TipoImovelRepositoryPort tipoImovelRepository) {
        this.tipoImovelRepository = tipoImovelRepository;
    }

    public TipoImovelResponse execute(Long id, UpdateTipoImovelRequest request, AuthenticatedUser currentUser) {
        validateAdmin(currentUser);

        TipoImovel existing = tipoImovelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de imóvel com ID " + id + " não encontrado."));

        TipoImovel updated = new TipoImovel(
                existing.id(),
                request.nomeTipo() != null ? request.nomeTipo() : existing.nomeTipo()
        );

        return TipoImovelMapper.toResponse(tipoImovelRepository.save(updated));
    }

    private void validateAdmin(AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Acesso negado");
        }
    }
}
