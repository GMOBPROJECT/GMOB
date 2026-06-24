package br.com.gmob.imovel.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataImagemImovelRepository extends JpaRepository<ImagemImovelJpaEntity, Long> {

    List<ImagemImovelJpaEntity> findByImovel_ImovelId(Long imovelId);

    void deleteByImovel_ImovelId(Long imovelId);
}
