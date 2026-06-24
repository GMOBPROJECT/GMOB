package br.com.gmob.cliente.api.dto;

import br.com.gmob.infra.domain.enums.TipoInteresseCliente;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ClienteResponse(
        @JsonProperty("cliente_id")
        Long clienteId,

        @JsonProperty("corretor_id")
        Long corretorId,

        String nome,

        String cpf,

        String email,

        String telefone,

        @JsonProperty("tipo_interesse")
        TipoInteresseCliente tipoInteresse,

        boolean arquivado,

        @JsonProperty("data_cadastro")
        Instant dataCadastro
) {
}
