package br.com.gmob.visita.api.dto;

import br.com.gmob.infra.domain.enums.StatusAgendamento;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateVisitaRequest(
        @JsonProperty("imovel_id")
        Long imovelId,

        @JsonProperty("cliente_id")
        Long clienteId,

        @JsonProperty("data_visita")
        String dataVisita,

        @JsonProperty("hora_inicio")
        String horaInicio,

        @JsonProperty("hora_termino")
        String horaTermino,

        String observacoes,

        @JsonProperty("status_agendamento")
        StatusAgendamento statusAgendamento
) {
}
