package br.com.gmob.cliente.infrastructure.persistence;

import br.com.gmob.cliente.domain.model.Cliente;
import br.com.gmob.cliente.domain.port.ClienteRepositoryPort;
import br.com.gmob.infra.domain.enums.TipoInteresseCliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final SpringDataClienteRepository repository;

    public ClienteRepositoryAdapter(SpringDataClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        return toDomain(repository.save(toEntity(cliente)));
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Cliente> findByEmail(String email) {
        return repository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<Cliente> findByCpfExcludingId(String cpf, Long excludeId) {
        return repository.findByCpfAndClienteIdNot(cpf, excludeId).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return repository.findByCpf(cpf).isPresent();
    }

    @Override
    public List<Cliente> findAll(Long corretorId, TipoInteresseCliente tipoInteresse, int page, int limit) {
        Page<ClienteJpaEntity> result = fetchPage(corretorId, tipoInteresse, page, limit);
        return result.stream().map(this::toDomain).toList();
    }

    @Override
    public long count(Long corretorId, TipoInteresseCliente tipoInteresse) {
        if (corretorId != null) {
            if (tipoInteresse != null) {
                return repository.countByArquivadoFalseAndCorretorIdAndTipoInteresse(corretorId, tipoInteresse);
            }
            return repository.countByArquivadoFalseAndCorretorId(corretorId);
        }
        if (tipoInteresse != null) {
            return repository.countByArquivadoFalseAndTipoInteresse(tipoInteresse);
        }
        return repository.countByArquivadoFalse();
    }

    private Page<ClienteJpaEntity> fetchPage(Long corretorId, TipoInteresseCliente tipoInteresse, int page, int limit) {
        PageRequest pageable = PageRequest.of(page - 1, limit);
        if (corretorId != null) {
            if (tipoInteresse != null) {
                return repository.findByArquivadoFalseAndCorretorIdAndTipoInteresseOrderByDataCadastroDesc(
                        corretorId, tipoInteresse, pageable
                );
            }
            return repository.findByArquivadoFalseAndCorretorIdOrderByDataCadastroDesc(corretorId, pageable);
        }
        if (tipoInteresse != null) {
            return repository.findByArquivadoFalseAndTipoInteresseOrderByDataCadastroDesc(tipoInteresse, pageable);
        }
        return repository.findByArquivadoFalseOrderByDataCadastroDesc(pageable);
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

    private ClienteJpaEntity toEntity(Cliente cliente) {
        ClienteJpaEntity entity = new ClienteJpaEntity();
        entity.setClienteId(cliente.id());
        entity.setCorretorId(cliente.corretorId());
        entity.setNome(cliente.nome());
        entity.setCpf(cliente.cpf());
        entity.setEmail(cliente.email());
        entity.setTelefone(cliente.telefone());
        entity.setTipoInteresse(cliente.tipoInteresse());
        entity.setArquivado(cliente.arquivado());
        entity.setDataCadastro(cliente.dataCadastro());
        return entity;
    }
}
