package br.com.gmob.imovel.application;

import br.com.gmob.imovel.api.dto.CreateTipoImovelRequest;
import br.com.gmob.imovel.api.dto.TipoImovelResponse;
import br.com.gmob.imovel.api.mapper.TipoImovelMapper;
import br.com.gmob.imovel.domain.model.TipoImovel;
import br.com.gmob.imovel.domain.port.TipoImovelRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ConflictException;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class CreateTipoImovelUseCase {

    private final TipoImovelRepositoryPort tipoImovelRepository;

    public CreateTipoImovelUseCase(TipoImovelRepositoryPort tipoImovelRepository) {
        this.tipoImovelRepository = tipoImovelRepository;
    }

    public TipoImovelResponse execute(CreateTipoImovelRequest request, AuthenticatedUser currentUser) {
        validateAdmin(currentUser);

        if (tipoImovelRepository.existsByNomeTipo(request.nomeTipo())) {
            throw new ConflictException("Este tipo de imóvel já existe.");
        }

        TipoImovel saved = tipoImovelRepository.save(new TipoImovel(null, request.nomeTipo()));
        return TipoImovelMapper.toResponse(saved);
    }

    private void validateAdmin(AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Acesso negado");
        }
    }
}
