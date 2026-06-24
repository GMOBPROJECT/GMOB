package br.com.gmob.cliente.domain.port;

import br.com.gmob.cliente.domain.model.Cliente;
import br.com.gmob.infra.domain.enums.TipoInteresseCliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {

    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Long id);

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByCpfExcludingId(String cpf, Long excludeId);

    List<Cliente> findAll(Long corretorId, TipoInteresseCliente tipoInteresse, int page, int limit);

    long count(Long corretorId, TipoInteresseCliente tipoInteresse);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
