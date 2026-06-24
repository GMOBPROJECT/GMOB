package br.com.gmob.imovel.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImagemImovelResponse(
        @JsonProperty("imagem_id")
        Long imagemId,

        String url,

        @JsonProperty("imovel_id")
        Long imovelId
) {
}
