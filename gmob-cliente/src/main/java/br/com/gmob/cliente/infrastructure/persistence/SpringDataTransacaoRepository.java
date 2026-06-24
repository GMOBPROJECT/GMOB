package br.com.gmob.cliente.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataTransacaoRepository extends JpaRepository<TransacaoImovelJpaEntity, Long> {

    @EntityGraph(attributePaths = {"imovel", "cliente"})
    Page<TransacaoImovelJpaEntity> findByCorretorIdOrderByDataTransacaoDesc(
            Long corretorId,
            Pageable pageable
    );

    long countByCorretorId(Long corretorId);

    @EntityGraph(attributePaths = {"imovel", "cliente"})
    Optional<TransacaoImovelJpaEntity> findByTransacaoId(Long transacaoId);
}
