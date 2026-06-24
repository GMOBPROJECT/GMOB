package br.com.gmob.cliente.infrastructure.persistence;

import br.com.gmob.infra.domain.enums.TipoInteresseCliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataClienteRepository extends JpaRepository<ClienteJpaEntity, Long> {

    Optional<ClienteJpaEntity> findByCpf(String cpf);

    Optional<ClienteJpaEntity> findByEmail(String email);

    Optional<ClienteJpaEntity> findByCpfAndClienteIdNot(String cpf, Long clienteId);

    Page<ClienteJpaEntity> findByArquivadoFalseAndCorretorIdOrderByDataCadastroDesc(
            Long corretorId,
            Pageable pageable
    );

    Page<ClienteJpaEntity> findByArquivadoFalseAndCorretorIdAndTipoInteresseOrderByDataCadastroDesc(
            Long corretorId,
            TipoInteresseCliente tipoInteresse,
            Pageable pageable
    );

    Page<ClienteJpaEntity> findByArquivadoFalseOrderByDataCadastroDesc(Pageable pageable);

    Page<ClienteJpaEntity> findByArquivadoFalseAndTipoInteresseOrderByDataCadastroDesc(
            TipoInteresseCliente tipoInteresse,
            Pageable pageable
    );

    long countByArquivadoFalseAndCorretorId(Long corretorId);

    long countByArquivadoFalseAndCorretorIdAndTipoInteresse(Long corretorId, TipoInteresseCliente tipoInteresse);

    long countByArquivadoFalse();

    long countByArquivadoFalseAndTipoInteresse(TipoInteresseCliente tipoInteresse);
}
