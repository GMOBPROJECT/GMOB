package br.com.gmob.visita.infrastructure.persistence;

import br.com.gmob.infra.domain.enums.StatusAgendamento;
import br.com.gmob.visita.domain.model.Visita;
import br.com.gmob.visita.domain.port.VisitaRepositoryPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
public class VisitaRepositoryAdapter implements VisitaRepositoryPort {

    private final SpringDataVisitaRepository repository;

    public VisitaRepositoryAdapter(SpringDataVisitaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Visita save(Visita visita) {
        VisitaJpaEntity entity = toEntity(visita);
        VisitaJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Visita> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByClienteIdAndImovelIdAndDataVisita(Long clienteId, Long imovelId, LocalDate dataVisita) {
        return repository.existsByClienteIdAndImovelIdAndDataVisita(clienteId, imovelId, dataVisita);
    }

    @Override
    public boolean existsConflictOnUpdate(
            Long agendamentoId,
            Long imovelId,
            Long clienteId,
            LocalDate dataVisita,
            LocalTime horaInicio
    ) {
        return repository.existsByImovelIdAndClienteIdAndDataVisitaAndHoraInicioAndAgendamentoIdNot(
                imovelId,
                clienteId,
                dataVisita,
                horaInicio,
                agendamentoId
        );
    }

    @Override
    public List<Visita> findAll(
            Long corretorId,
            Long imovelId,
            Long clienteId,
            LocalDate dataVisita,
            StatusAgendamento status,
            int page,
            int limit
    ) {
        Specification<VisitaJpaEntity> spec = VisitaSpecifications.withFilters(
                corretorId,
                imovelId,
                clienteId,
                dataVisita,
                status
        );
        int pageIndex = Math.max(page - 1, 0);
        return repository.findAll(
                        spec,
                        PageRequest.of(pageIndex, limit, Sort.by("dataVisita").ascending())
                )
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public long count(
            Long corretorId,
            Long imovelId,
            Long clienteId,
            LocalDate dataVisita,
            StatusAgendamento status
    ) {
        return repository.count(VisitaSpecifications.withFilters(
                corretorId,
                imovelId,
                clienteId,
                dataVisita,
                status
        ));
    }

    @Override
    @Transactional
    public void deleteByImovelId(Long imovelId, Long corretorId, boolean isAdmin) {
        if (isAdmin) {
            repository.deleteByImovelId(imovelId);
        } else {
            repository.deleteByImovelIdAndCorretorId(imovelId, corretorId);
        }
    }

    private VisitaJpaEntity toEntity(Visita visita) {
        VisitaJpaEntity entity = new VisitaJpaEntity();
        entity.setAgendamentoId(visita.agendamentoId());
        entity.setCorretorId(visita.corretorId());
        entity.setImovelId(visita.imovelId());
        entity.setClienteId(visita.clienteId());
        entity.setDataVisita(visita.dataVisita());
        entity.setHoraInicio(visita.horaInicio());
        entity.setHoraTermino(visita.horaTermino());
        entity.setObservacoes(visita.observacoes());
        entity.setStatusAgendamento(visita.statusAgendamento());
        entity.setDataAgendamento(visita.dataAgendamento());
        return entity;
    }

    private Visita toDomain(VisitaJpaEntity entity) {
        return new Visita(
                entity.getAgendamentoId(),
                entity.getCorretorId(),
                entity.getImovelId(),
                entity.getClienteId(),
                entity.getDataVisita(),
                entity.getHoraInicio(),
                entity.getHoraTermino(),
                entity.getObservacoes(),
                entity.getStatusAgendamento(),
                entity.getDataAgendamento()
        );
    }
}
