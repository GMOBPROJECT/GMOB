package br.com.gmob.visita.infrastructure.persistence;

import br.com.gmob.imovel.domain.model.Imovel;
import br.com.gmob.imovel.domain.port.ImovelRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import br.com.gmob.visita.application.port.VisitaImovelPort;
import org.springframework.stereotype.Component;

@Component
public class VisitaImovelAdapter implements VisitaImovelPort {

    private final ImovelRepositoryPort imovelRepository;

    public VisitaImovelAdapter(ImovelRepositoryPort imovelRepository) {
        this.imovelRepository = imovelRepository;
    }

    @Override
    public ImovelVisitaView findByIdForVisita(Long imovelId, AuthenticatedUser user) {
        Imovel imovel = imovelRepository.findById(imovelId)
                .orElseThrow(() -> new ResourceNotFoundException("Imóvel com ID " + imovelId + " não encontrado."));

        if (user.perfil() != Perfil.ADMINISTRADOR && !imovel.corretorId().equals(user.corretorId())) {
            throw new ForbiddenException("Você não tem permissão para acessar este recurso.");
        }

        return new ImovelVisitaView(imovel.id(), imovel.corretorId(), formatEndereco(imovel));
    }

    private String formatEndereco(Imovel imovel) {
        StringBuilder endereco = new StringBuilder();
        endereco.append(imovel.rua()).append(", ").append(imovel.numero());
        if (imovel.complemento() != null && !imovel.complemento().isBlank()) {
            endereco.append(" - ").append(imovel.complemento());
        }
        endereco.append(" - ").append(imovel.cidade()).append("/").append(imovel.estado());
        return endereco.toString();
    }
}
