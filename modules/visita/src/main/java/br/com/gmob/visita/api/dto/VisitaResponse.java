package br.com.gmob.visita.api.dto;

import br.com.gmob.infra.domain.enums.StatusAgendamento;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public record VisitaResponse(
        @JsonProperty("agendamento_id")
        Long agendamentoId,

        @JsonProperty("corretor_id")
        Long corretorId,

        @JsonProperty("imovel_id")
        Long imovelId,

        @JsonProperty("cliente_id")
        Long clienteId,

        @JsonProperty("data_visita")
        LocalDate dataVisita,

        @JsonProperty("hora_inicio")
        LocalTime horaInicio,

        @JsonProperty("hora_termino")
        LocalTime horaTermino,

        String observacoes,

        @JsonProperty("status_agendamento")
        StatusAgendamento statusAgendamento,

        @JsonProperty("data_agendamento")
        Instant dataAgendamento
) {
}
