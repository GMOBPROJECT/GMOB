package br.com.gmob.corretor.domain.model;

import br.com.gmob.infra.domain.enums.Perfil;

import java.time.Instant;
import java.util.Optional;

public record Corretor(
        Long id,
        String nomeCompleto,
        String email,
        String telefone,
        String cpf,
        Optional<String> senha,
        Perfil perfil,
        Instant dataCadastro
) {
    public Corretor withoutSenha() {
        return new Corretor(id, nomeCompleto, email, telefone, cpf, Optional.empty(), perfil, dataCadastro);
    }
}
