package br.com.gmob.imovel.application;

import br.com.gmob.imovel.api.dto.ImovelResponse;
import br.com.gmob.imovel.api.dto.UpdateImovelRequest;
import br.com.gmob.imovel.api.mapper.ImovelMapper;
import br.com.gmob.imovel.domain.model.Imovel;
import br.com.gmob.imovel.domain.port.ImovelRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class UpdateImovelUseCase {

    private final ImovelRepositoryPort imovelRepository;
    private final FindImovelUseCase findImovelUseCase;

    public UpdateImovelUseCase(ImovelRepositoryPort imovelRepository, FindImovelUseCase findImovelUseCase) {
        this.imovelRepository = imovelRepository;
        this.findImovelUseCase = findImovelUseCase;
    }

    public ImovelResponse execute(Long id, UpdateImovelRequest request, AuthenticatedUser currentUser) {
        validateRole(currentUser);
        findImovelUseCase.execute(id, currentUser);

        Imovel current = imovelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imóvel com ID " + id + " não encontrado."));

        Imovel updated = new Imovel(
                current.id(),
                current.corretorId(),
                request.tipoImovelId() != null ? request.tipoImovelId() : current.tipoImovelId(),
                current.status(),
                request.valorAluguel() != null ? request.valorAluguel() : current.valorAluguel(),
                request.disponibilidade() != null ? request.disponibilidade() : current.disponibilidade(),
                request.estado() != null ? request.estado() : current.estado(),
                request.cidade() != null ? request.cidade() : current.cidade(),
                request.rua() != null ? request.rua() : current.rua(),
                request.numero() != null ? request.numero() : current.numero(),
                request.complemento() != null ? request.complemento() : current.complemento(),
                request.valor() != null ? request.valor() : current.valor(),
                request.area() != null ? request.area() : current.area(),
                request.numeroComodos() != null ? request.numeroComodos() : current.numeroComodos(),
                request.descricao() != null ? request.descricao() : current.descricao(),
                current.dataCadastro(),
                current.imagens(),
                current.corretorNomeCompleto()
        );

        return ImovelMapper.toResponse(imovelRepository.save(updated));
    }

    private void validateRole(AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.CORRETOR && currentUser.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Acesso negado");
        }
    }
}
