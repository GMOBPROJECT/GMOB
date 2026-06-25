package br.com.gmob.imovel.application;

import br.com.gmob.imovel.api.dto.ImovelResponse;
import br.com.gmob.imovel.api.mapper.ImovelMapper;
import br.com.gmob.imovel.domain.model.Imovel;
import br.com.gmob.imovel.domain.port.ImagemImovelRepositoryPort;
import br.com.gmob.imovel.domain.port.ImovelRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.event.ImovelRemovidoEvent;
import br.com.gmob.infra.exception.ConflictException;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteImovelUseCase {

    private final ImovelRepositoryPort imovelRepository;
    private final ImagemImovelRepositoryPort imagemRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final FindImovelUseCase findImovelUseCase;

    public DeleteImovelUseCase(
            ImovelRepositoryPort imovelRepository,
            ImagemImovelRepositoryPort imagemRepository,
            ApplicationEventPublisher eventPublisher,
            FindImovelUseCase findImovelUseCase
    ) {
        this.imovelRepository = imovelRepository;
        this.imagemRepository = imagemRepository;
        this.eventPublisher = eventPublisher;
        this.findImovelUseCase = findImovelUseCase;
    }

    @Transactional
    public ImovelResponse execute(Long id, AuthenticatedUser currentUser) {
        validateRole(currentUser);
        findImovelUseCase.execute(id, currentUser);

        Imovel current = imovelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imóvel com ID " + id + " não encontrado."));

        if (imovelRepository.existsTransacaoByImovelId(id)) {
            throw new ConflictException("Este imóvel não pode ser excluído pois possui histórico.");
        }

        boolean isAdmin = currentUser.perfil() == Perfil.ADMINISTRADOR;
        imagemRepository.deleteByImovelId(id);
        imovelRepository.delete(id);

        eventPublisher.publishEvent(new ImovelRemovidoEvent(id, currentUser.corretorId(), isAdmin));

        return ImovelMapper.toResponse(current.withoutImagens());
    }

    private void validateRole(AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.CORRETOR && currentUser.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Acesso negado");
        }
    }
}
