package br.com.gmob.visita.infrastructure.persistence;

import br.com.gmob.infra.domain.enums.StatusAgendamento;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

final class VisitaSpecifications {

    private VisitaSpecifications() {
    }

    static Specification<VisitaJpaEntity> withFilters(
            Long corretorId,
            Long imovelId,
            Long clienteId,
            LocalDate dataVisita,
            StatusAgendamento status
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (corretorId != null) {
                predicates.add(cb.equal(root.get("corretorId"), corretorId));
            }
            if (imovelId != null) {
                predicates.add(cb.equal(root.get("imovelId"), imovelId));
            }
            if (clienteId != null) {
                predicates.add(cb.equal(root.get("clienteId"), clienteId));
            }
            if (dataVisita != null) {
                predicates.add(cb.equal(root.get("dataVisita"), dataVisita));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("statusAgendamento"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
