package br.com.gmob.visita.domain.event;

import java.time.LocalDate;
import java.time.LocalTime;

public record VisitaAgendadaEvent(
        Long agendamentoId,
        Long corretorId,
        Long imovelId,
        Long clienteId,
        LocalDate dataVisita,
        LocalTime horaInicio,
        LocalTime horaTermino,
        String observacoes,
        String imovelEndereco,
        String clienteNome,
        String clienteEmail,
        String corretorNome
) {
}
