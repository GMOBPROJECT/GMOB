package br.com.gmob.visita.api.mapper;

import br.com.gmob.visita.api.dto.VisitaResponse;
import br.com.gmob.visita.domain.model.Visita;

public final class VisitaMapper {

    private VisitaMapper() {
    }

    public static VisitaResponse toResponse(Visita visita) {
        return new VisitaResponse(
                visita.agendamentoId(),
                visita.corretorId(),
                visita.imovelId(),
                visita.clienteId(),
                visita.dataVisita(),
                visita.horaInicio(),
                visita.horaTermino(),
                visita.observacoes(),
                visita.statusAgendamento(),
                visita.dataAgendamento()
        );
    }
}
