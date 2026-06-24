package br.com.gmob.imovel.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataTipoImovelRepository extends JpaRepository<TipoImovelJpaEntity, Long> {

    Optional<TipoImovelJpaEntity> findByNomeTipo(String nomeTipo);

    boolean existsByNomeTipo(String nomeTipo);
}
