package br.com.gmob.visita.application.port;

import br.com.gmob.infra.domain.enums.StatusAgendamento;
import br.com.gmob.infra.security.AuthenticatedUser;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface VisitaQueryPort {

    List<VisitaPendenteView> findAgendadosByImovelId(Long imovelId);

    record VisitaPendenteView(
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
