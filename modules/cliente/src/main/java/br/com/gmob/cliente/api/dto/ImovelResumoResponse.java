package br.com.gmob.cliente.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ImovelResumoResponse(
        @JsonProperty("imovel_id")
        Long imovelId,

        String rua,

        String numero,

        String cidade,

        BigDecimal valor,

        @JsonProperty("valor_aluguel")
        BigDecimal valorAluguel
) {
}
