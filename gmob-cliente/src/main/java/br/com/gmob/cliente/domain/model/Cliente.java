package br.com.gmob.cliente.domain.model;

import br.com.gmob.infra.domain.enums.TipoInteresseCliente;

import java.time.Instant;

public record Cliente(
        Long id,
        Long corretorId,
        String nome,
        String cpf,
        String email,
        String telefone,
        TipoInteresseCliente tipoInteresse,
        boolean arquivado,
        Instant dataCadastro
) {
}
