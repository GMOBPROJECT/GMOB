package br.com.gmob.visita.domain.model;

import br.com.gmob.infra.domain.enums.StatusAgendamento;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public record Visita(
        Long agendamentoId,
        Long corretorId,
        Long imovelId,
        Long clienteId,
        LocalDate dataVisita,
        LocalTime horaInicio,
        LocalTime horaTermino,
        String observacoes,
        StatusAgendamento statusAgendamento,
        Instant dataAgendamento
) {
}
