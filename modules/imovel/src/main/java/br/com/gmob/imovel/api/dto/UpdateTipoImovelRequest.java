package br.com.gmob.imovel.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateTipoImovelRequest(
        @JsonProperty("nome_tipo")
        String nomeTipo
) {
}
