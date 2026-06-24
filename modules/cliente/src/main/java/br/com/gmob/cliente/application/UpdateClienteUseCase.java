package br.com.gmob.cliente.application;

import br.com.gmob.cliente.api.dto.ClienteResponse;
import br.com.gmob.cliente.api.dto.UpdateClienteRequest;
import br.com.gmob.cliente.api.mapper.ClienteMapper;
import br.com.gmob.cliente.domain.model.Cliente;
import br.com.gmob.cliente.domain.port.ClienteRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class UpdateClienteUseCase {

    private final ClienteRepositoryPort clienteRepository;

    public UpdateClienteUseCase(ClienteRepositoryPort clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponse execute(Long id, UpdateClienteRequest request, AuthenticatedUser currentUser) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        boolean isOwner = cliente.corretorId().equals(currentUser.corretorId());
        boolean isAdmin = currentUser.perfil() == Perfil.ADMINISTRADOR;

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("Você não pode atualizar este cliente");
        }

        if (request.cpf() != null) {
            clienteRepository.findByCpfExcludingId(request.cpf(), id).ifPresent(existing -> {
                throw new ForbiddenException("Já existe um cliente com este CPF");
            });
        }

        Cliente updated = clienteRepository.save(new Cliente(
                cliente.id(),
                cliente.corretorId(),
                request.nomeCompleto() != null ? request.nomeCompleto() : cliente.nome(),
                request.cpf() != null ? request.cpf() : cliente.cpf(),
                request.email() != null ? request.email() : cliente.email(),
                request.telefone() != null ? request.telefone() : cliente.telefone(),
                request.tipoInteresse() != null ? request.tipoInteresse() : cliente.tipoInteresse(),
                cliente.arquivado(),
                cliente.dataCadastro()
        ));

        return ClienteMapper.toResponse(updated);
    }
}
