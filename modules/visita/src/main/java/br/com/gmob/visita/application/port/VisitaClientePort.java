package br.com.gmob.visita.application.port;

import br.com.gmob.infra.security.AuthenticatedUser;

public interface VisitaClientePort {

    ClienteVisitaView findByIdForVisita(Long clienteId, AuthenticatedUser user);

    record ClienteVisitaView(
            Long id,
            String nome,
            String email,
            String telefone
    ) {
    }
}
