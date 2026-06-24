package br.com.gmob.cliente.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClienteResumoResponse(
        @JsonProperty("cliente_id")
        Long clienteId,

        String nome
) {
}
