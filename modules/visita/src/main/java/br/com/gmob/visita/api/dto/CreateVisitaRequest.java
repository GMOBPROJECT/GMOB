package br.com.gmob.visita.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateVisitaRequest(
        @JsonProperty("imovel_id")
        @NotNull(message = "O ID do imóvel é obrigatório.")
        Long imovelId,

        @JsonProperty("cliente_id")
        @NotNull(message = "O ID do cliente é obrigatório.")
        Long clienteId,

        @JsonProperty("data_visita")
        @NotBlank(message = "A data da visita é obrigatória.")
        String dataVisita,

        @JsonProperty("hora_inicio")
        @NotBlank(message = "O horário de início é obrigatório.")
        String horaInicio,

        @JsonProperty("hora_termino")
        @NotBlank(message = "O horário de término é obrigatório.")
        String horaTermino,

        String observacoes
) {
}
