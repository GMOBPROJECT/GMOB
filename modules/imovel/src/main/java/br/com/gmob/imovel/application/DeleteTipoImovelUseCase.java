package br.com.gmob.imovel.application;

import br.com.gmob.imovel.api.dto.TipoImovelResponse;
import br.com.gmob.imovel.api.mapper.TipoImovelMapper;
import br.com.gmob.imovel.domain.model.TipoImovel;
import br.com.gmob.imovel.domain.port.TipoImovelRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ConflictException;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class DeleteTipoImovelUseCase {

    private final TipoImovelRepositoryPort tipoImovelRepository;

    public DeleteTipoImovelUseCase(TipoImovelRepositoryPort tipoImovelRepository) {
        this.tipoImovelRepository = tipoImovelRepository;
    }

    public TipoImovelResponse execute(Long id, AuthenticatedUser currentUser) {
        validateAdmin(currentUser);

        TipoImovel existing = tipoImovelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de imóvel com ID " + id + " não encontrado."));

        if (tipoImovelRepository.countImoveisByTipoImovelId(id) > 0) {
            throw new ConflictException(
                    "Não é possível remover este tipo, pois existem imóveis vinculados a ele."
            );
        }

        tipoImovelRepository.delete(id);
        return TipoImovelMapper.toResponse(existing);
    }

    private void validateAdmin(AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Acesso negado");
        }
    }
}
