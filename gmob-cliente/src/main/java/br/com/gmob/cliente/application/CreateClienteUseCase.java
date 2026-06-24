package br.com.gmob.cliente.application;

import br.com.gmob.cliente.api.dto.CreateClienteRequest;
import br.com.gmob.cliente.api.dto.ClienteResponse;
import br.com.gmob.cliente.api.mapper.ClienteMapper;
import br.com.gmob.cliente.domain.model.Cliente;
import br.com.gmob.cliente.domain.port.ClienteRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreateClienteUseCase {

    private final ClienteRepositoryPort clienteRepository;

    public CreateClienteUseCase(ClienteRepositoryPort clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponse execute(CreateClienteRequest request, AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.CORRETOR && currentUser.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Apenas corretores ou admins podem cadastrar clientes");
        }

        if (clienteRepository.existsByEmail(request.email())) {
            throw new ForbiddenException("Já existe um cliente com este email");
        }

        if (clienteRepository.existsByCpf(request.cpf())) {
            throw new ForbiddenException("Já existe um cliente com este CPF");
        }

        Cliente cliente = clienteRepository.save(new Cliente(
                null,
                currentUser.corretorId(),
                request.nomeCompleto(),
                request.cpf(),
                request.email(),
                request.telefone(),
                request.tipoInteresse(),
                false,
                Instant.now()
        ));

        return ClienteMapper.toResponse(cliente);
    }
}
