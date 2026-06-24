package br.com.gmob.corretor.application.port;

import br.com.gmob.corretor.domain.model.Corretor;

import java.util.Optional;

public interface CorretorAuthPort {

    Optional<Corretor> findByEmailWithPassword(String email);

    Optional<Corretor> findByIdForAuth(Long id);
}
