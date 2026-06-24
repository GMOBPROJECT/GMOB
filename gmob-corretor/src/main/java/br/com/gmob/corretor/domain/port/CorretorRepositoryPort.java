package br.com.gmob.corretor.domain.port;

import br.com.gmob.corretor.domain.model.Corretor;

import java.util.List;
import java.util.Optional;

public interface CorretorRepositoryPort {

    Corretor save(Corretor corretor);

    Optional<Corretor> findById(Long id);

    Optional<Corretor> findByEmail(String email);

    Optional<Corretor> findByCpf(String cpf);

    List<Corretor> findAll(int page, int limit);

    long count();

    void delete(Long id);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
