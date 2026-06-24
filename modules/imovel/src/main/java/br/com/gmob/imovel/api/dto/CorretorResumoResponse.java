package br.com.gmob.imovel.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CorretorResumoResponse(
        @JsonProperty("nome_completo")
        String nomeCompleto
) {
}
