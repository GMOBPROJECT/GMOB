package br.com.gmob.visita.application.port;

import br.com.gmob.infra.security.AuthenticatedUser;

public interface VisitaImovelPort {

    ImovelVisitaView findByIdForVisita(Long imovelId, AuthenticatedUser user);

    record ImovelVisitaView(
            Long id,
            Long corretorId,
            String enderecoFormatado
    ) {
    }
}
