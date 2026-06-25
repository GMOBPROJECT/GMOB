package br.com.gmob.visita.domain.port;

import br.com.gmob.infra.domain.enums.StatusAgendamento;
import br.com.gmob.visita.domain.model.Visita;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VisitaRepositoryPort {

    Visita save(Visita visita);

    Optional<Visita> findById(Long id);

    void deleteById(Long id);

    boolean existsByClienteIdAndImovelIdAndDataVisita(Long clienteId, Long imovelId, LocalDate dataVisita);

    boolean existsConflictOnUpdate(
            Long agendamentoId,
            Long imovelId,
            Long clienteId,
            LocalDate dataVisita,
            java.time.LocalTime horaInicio
    );

    List<Visita> findAll(
            Long corretorId,
            Long imovelId,
            Long clienteId,
            LocalDate dataVisita,
            StatusAgendamento status,
            int page,
            int limit
    );

    long count(Long corretorId, Long imovelId, Long clienteId, LocalDate dataVisita, StatusAgendamento status);

    void deleteByImovelId(Long imovelId, Long corretorId, boolean isAdmin);
}
