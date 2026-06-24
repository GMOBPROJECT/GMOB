package br.com.gmob.infra.security;

import br.com.gmob.infra.domain.enums.Perfil;

import java.time.Instant;

public record AuthenticatedUser(
        Long corretorId,
        String nomeCompleto,
        String email,
        String telefone,
        String cpf,
        Perfil perfil,
        Instant dataCadastro
) {
}
