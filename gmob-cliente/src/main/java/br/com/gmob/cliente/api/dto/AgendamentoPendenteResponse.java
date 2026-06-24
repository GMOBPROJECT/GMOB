package br.com.gmob.cliente.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AgendamentoPendenteResponse(
        @JsonProperty("agendamento_id")
        Long agendamentoId,

        @JsonProperty("imovel_id")
        Long imovelId,

        @JsonProperty("corretor_id")
        Long corretorId,

        @JsonProperty("cliente_id")
        Long clienteId,

        @JsonProperty("data_visita")
        java.time.LocalDate dataVisita,

        @JsonProperty("hora_inicio")
        java.time.LocalTime horaInicio,

        @JsonProperty("hora_termino")
        java.time.LocalTime horaTermino,

        String observacoes,

        @JsonProperty("status_agendamento")
        br.com.gmob.infra.domain.enums.StatusAgendamento statusAgendamento,

        @JsonProperty("data_agendamento")
        java.time.Instant dataAgendamento,

        ClienteAgendamentoResumoResponse cliente
) {
}
