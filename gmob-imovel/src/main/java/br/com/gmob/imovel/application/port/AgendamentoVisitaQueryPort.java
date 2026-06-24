package br.com.gmob.imovel.application.port;

import br.com.gmob.infra.domain.enums.StatusAgendamento;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoVisitaQueryPort {

    List<AgendamentoPendenteView> findAgendadosByImovelId(Long imovelId);

    record AgendamentoPendenteView(
            Long agendamentoId,
            Long imovelId,
            Long corretorId,
            Long clienteId,
            LocalDate dataVisita,
            LocalTime horaInicio,
            LocalTime horaTermino,
            String observacoes,
            StatusAgendamento statusAgendamento,
            Instant dataAgendamento,
            String clienteNome,
            String clienteTelefone,
            String clienteEmail
    ) {
    }
}
