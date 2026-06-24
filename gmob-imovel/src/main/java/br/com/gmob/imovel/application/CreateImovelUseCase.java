package br.com.gmob.imovel.application;

import br.com.gmob.imovel.api.dto.CreateImovelRequest;
import br.com.gmob.imovel.api.dto.ImovelResponse;
import br.com.gmob.imovel.api.mapper.ImovelMapper;
import br.com.gmob.imovel.domain.model.Imovel;
import br.com.gmob.imovel.domain.port.ImovelRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.domain.enums.StatusImovel;
import br.com.gmob.infra.exception.ConflictException;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreateImovelUseCase {

    private final ImovelRepositoryPort imovelRepository;

    public CreateImovelUseCase(ImovelRepositoryPort imovelRepository) {
        this.imovelRepository = imovelRepository;
    }

    public ImovelResponse execute(CreateImovelRequest request, AuthenticatedUser currentUser) {
        validateRole(currentUser);

        if (imovelRepository.existsByAddress(
                request.rua(),
                request.numero(),
                request.complemento(),
                request.cidade(),
                request.estado()
        )) {
            throw new ConflictException("Um imóvel com este endereço já foi cadastrado.");
        }

        Imovel imovel = imovelRepository.save(new Imovel(
                null,
                currentUser.corretorId(),
                request.tipoImovelId(),
                StatusImovel.DISPONIVEL,
                request.valorAluguel(),
                request.disponibilidade(),
                request.estado(),
                request.cidade(),
                request.rua(),
                request.numero(),
                request.complemento(),
                request.valor(),
                request.area(),
                request.numeroComodos(),
                request.descricao(),
                Instant.now(),
                null,
                null
        ));

        return ImovelMapper.toResponse(imovel);
    }

    private void validateRole(AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.CORRETOR && currentUser.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Acesso negado");
        }
    }
}
