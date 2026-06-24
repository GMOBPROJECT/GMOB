package br.com.gmob.imovel.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataImovelRepository extends JpaRepository<ImovelJpaEntity, Long>,
        JpaSpecificationExecutor<ImovelJpaEntity> {

    @Query("""
            SELECT i FROM ImovelJpaEntity i
            LEFT JOIN FETCH i.imagens
            LEFT JOIN FETCH i.corretor
            WHERE i.imovelId = :id
            """)
    Optional<ImovelJpaEntity> findByIdWithImagens(@Param("id") Long id);

    @Query("""
            SELECT COUNT(i) > 0 FROM ImovelJpaEntity i
            WHERE i.rua = :rua
            AND i.numero = :numero
            AND ((:complemento IS NULL AND i.complemento IS NULL) OR i.complemento = :complemento)
            AND i.cidade = :cidade
            AND i.estado = :estado
            """)
    boolean existsByAddress(
            @Param("rua") String rua,
            @Param("numero") String numero,
            @Param("complemento") String complemento,
            @Param("cidade") String cidade,
            @Param("estado") String estado
    );

    long countByTipoImovel_TipoImovelId(Long tipoImovelId);

    @EntityGraph(attributePaths = {"imagens", "corretor"})
    Page<ImovelJpaEntity> findAll(Specification<ImovelJpaEntity> spec, Pageable pageable);
}
