package br.com.gmob.cliente.infrastructure.persistence;

import br.com.gmob.cliente.domain.model.Cliente;
import br.com.gmob.cliente.domain.port.ClienteRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import br.com.gmob.visita.application.port.VisitaClientePort;
import org.springframework.stereotype.Component;

@Component
public class VisitaClienteAdapter implements VisitaClientePort {

    private final ClienteRepositoryPort clienteRepository;

    public VisitaClienteAdapter(ClienteRepositoryPort clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public ClienteVisitaView findByIdForVisita(Long clienteId, AuthenticatedUser user) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + clienteId + " não encontrado."));

        if (user.perfil() != Perfil.ADMINISTRADOR && !cliente.corretorId().equals(user.corretorId())) {
            throw new ForbiddenException("Você não tem permissão para acessar este recurso.");
        }

        return new ClienteVisitaView(cliente.id(), cliente.nome(), cliente.email(), cliente.telefone());
    }
}
