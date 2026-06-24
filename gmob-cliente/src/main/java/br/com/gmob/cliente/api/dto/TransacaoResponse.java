package br.com.gmob.cliente.api.dto;

import br.com.gmob.infra.domain.enums.TipoTransacao;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record TransacaoResponse(
        @JsonProperty("transacao_id")
        Long transacaoId,

        @JsonProperty("imovel_id")
        Long imovelId,

        @JsonProperty("cliente_id")
        Long clienteId,

        @JsonProperty("corretor_id")
        Long corretorId,

        @JsonProperty("tipo_transacao")
        TipoTransacao tipoTransacao,

        @JsonProperty("data_transacao")
        Instant dataTransacao
) {
}
