package br.com.gmob.imovel.infrastructure.persistence;

import br.com.gmob.imovel.application.port.AgendamentoVisitaQueryPort;
import br.com.gmob.infra.domain.enums.StatusAgendamento;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class AgendamentoVisitaQueryAdapter implements AgendamentoVisitaQueryPort {

    private final SpringDataAgendamentoVisitaRepository repository;

    public AgendamentoVisitaQueryAdapter(SpringDataAgendamentoVisitaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AgendamentoPendenteView> findAgendadosByImovelId(Long imovelId) {
        return repository.findAgendadosWithClienteByImovelId(imovelId)
                .stream()
                .map(this::mapRow)
                .toList();
    }

    private AgendamentoPendenteView mapRow(Object[] row) {
        return new AgendamentoPendenteView(
                asLong(row[0]),
                asLong(row[1]),
                asLong(row[2]),
                asLong(row[3]),
                asLocalDate(row[4]),
                asLocalTime(row[5]),
                asLocalTime(row[6]),
                row[7] != null ? row[7].toString() : null,
                StatusAgendamento.fromValue(row[8].toString()),
                asInstant(row[9]),
                row[10] != null ? row[10].toString() : null,
                row[11] != null ? row[11].toString() : null,
                row[12] != null ? row[12].toString() : null
        );
    }

    private Long asLong(Object value) {
        return value != null ? ((Number) value).longValue() : null;
    }

    private LocalDate asLocalDate(Object value) {
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof Date date) {
            return date.toLocalDate();
        }
        return null;
    }

    private LocalTime asLocalTime(Object value) {
        if (value instanceof LocalTime localTime) {
            return localTime;
        }
        if (value instanceof Time time) {
            return time.toLocalTime();
        }
        return null;
    }

    private Instant asInstant(Object value) {
        if (value instanceof Instant instant) {
            return instant;
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toInstant();
        }
        return null;
    }
}
