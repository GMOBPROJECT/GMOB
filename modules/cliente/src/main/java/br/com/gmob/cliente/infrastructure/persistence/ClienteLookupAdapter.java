package br.com.gmob.cliente.infrastructure.persistence;

import br.com.gmob.cliente.application.port.ClienteLookupPort;
import br.com.gmob.cliente.domain.model.Cliente;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ClienteLookupAdapter implements ClienteLookupPort {

    private final SpringDataClienteRepository repository;

    public ClienteLookupAdapter(SpringDataClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cliente findByCpf(String cpf) {
        return repository.findByCpf(cpf)
                .map(this::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente não encontrado com o CPF fornecido."
                ));
    }

    private Cliente toDomain(ClienteJpaEntity entity) {
        return new Cliente(
                entity.getClienteId(),
                entity.getCorretorId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getTipoInteresse(),
                entity.isArquivado(),
                entity.getDataCadastro()
        );
    }
}
