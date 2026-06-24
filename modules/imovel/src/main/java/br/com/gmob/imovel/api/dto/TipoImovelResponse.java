package br.com.gmob.imovel.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TipoImovelResponse(
        @JsonProperty("tipo_imovel_id")
        Long tipoImovelId,

        @JsonProperty("nome_tipo")
        String nomeTipo
) {
}
