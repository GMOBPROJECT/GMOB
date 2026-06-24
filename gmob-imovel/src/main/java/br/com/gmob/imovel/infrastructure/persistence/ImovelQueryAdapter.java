package br.com.gmob.imovel.infrastructure.persistence;

import br.com.gmob.imovel.application.port.ImovelQueryPort;
import br.com.gmob.imovel.domain.model.Imovel;
import br.com.gmob.imovel.domain.model.ImovelTransactionView;
import br.com.gmob.imovel.domain.port.ImovelRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.domain.enums.StatusImovel;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ImovelQueryAdapter implements ImovelQueryPort {

    private final ImovelRepositoryPort imovelRepository;

    public ImovelQueryAdapter(ImovelRepositoryPort imovelRepository) {
        this.imovelRepository = imovelRepository;
    }

    @Override
    public ImovelTransactionView findByIdForTransaction(Long id, AuthenticatedUser user) {
        Imovel imovel = imovelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imóvel com ID " + id + " não encontrado."));

        if (user.perfil() != Perfil.ADMINISTRADOR && !imovel.corretorId().equals(user.corretorId())) {
            throw new ForbiddenException("Você não tem permissão para acessar este recurso.");
        }

        return toTransactionView(imovel);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, StatusImovel status, AuthenticatedUser user) {
        Imovel existing = imovelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imóvel com ID " + id + " não encontrado."));

        if (user.perfil() != Perfil.ADMINISTRADOR && !existing.corretorId().equals(user.corretorId())) {
            throw new ForbiddenException("Você não tem permissão para acessar este recurso.");
        }

        Imovel updated = new Imovel(
                existing.id(),
                existing.corretorId(),
                existing.tipoImovelId(),
                status,
                existing.valorAluguel(),
                existing.disponibilidade(),
                existing.estado(),
                existing.cidade(),
                existing.rua(),
                existing.numero(),
                existing.complemento(),
                existing.valor(),
                existing.area(),
                existing.numeroComodos(),
                existing.descricao(),
                existing.dataCadastro(),
                existing.imagens(),
                existing.corretorNomeCompleto()
        );

        imovelRepository.save(updated);
    }

    private ImovelTransactionView toTransactionView(Imovel imovel) {
        return new ImovelTransactionView(imovel.id(), imovel.corretorId(), imovel.status());
    }
}
