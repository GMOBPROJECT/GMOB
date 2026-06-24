package br.com.gmob.imovel.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImagemUploadDataResponse(
        @JsonProperty("imovel_id")
        Long imovelId,

        String url
) {
}
