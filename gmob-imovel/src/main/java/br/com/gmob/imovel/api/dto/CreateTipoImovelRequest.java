package br.com.gmob.imovel.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateTipoImovelRequest(
        @JsonProperty("nome_tipo")
        @NotBlank(message = "O nome do tipo não pode ser vazio.")
        String nomeTipo
) {
}
