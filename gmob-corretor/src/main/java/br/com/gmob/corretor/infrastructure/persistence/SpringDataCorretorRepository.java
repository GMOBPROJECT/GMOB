package br.com.gmob.corretor.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataCorretorRepository extends JpaRepository<CorretorJpaEntity, Long> {

    Optional<CorretorJpaEntity> findByEmail(String email);

    Optional<CorretorJpaEntity> findByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Page<CorretorJpaEntity> findAllByOrderByDataCadastroDesc(Pageable pageable);
}
