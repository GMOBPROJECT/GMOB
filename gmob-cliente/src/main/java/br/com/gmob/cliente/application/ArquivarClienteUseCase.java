package br.com.gmob.cliente.application;

import br.com.gmob.cliente.api.dto.ClienteResponse;
import br.com.gmob.cliente.api.mapper.ClienteMapper;
import br.com.gmob.cliente.domain.model.Cliente;
import br.com.gmob.cliente.domain.port.ClienteRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class ArquivarClienteUseCase {

    private final ClienteRepositoryPort clienteRepository;

    public ArquivarClienteUseCase(ClienteRepositoryPort clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponse execute(Long id, AuthenticatedUser currentUser) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        boolean isOwner = cliente.corretorId().equals(currentUser.corretorId());
        boolean isAdmin = currentUser.perfil() == Perfil.ADMINISTRADOR;

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("Você não pode arquivar este cliente");
        }

        Cliente arquivado = clienteRepository.save(new Cliente(
                cliente.id(),
                cliente.corretorId(),
                cliente.nome(),
                cliente.cpf(),
                cliente.email(),
                cliente.telefone(),
                cliente.tipoInteresse(),
                true,
                cliente.dataCadastro()
        ));

        return ClienteMapper.toResponse(arquivado);
    }
}
