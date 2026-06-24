package br.com.gmob.imovel.infrastructure.persistence;

import br.com.gmob.infra.domain.enums.Disponibilidade;
import br.com.gmob.infra.domain.enums.StatusImovel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class ImovelSpecifications {

    private ImovelSpecifications() {
    }

    public static Specification<ImovelJpaEntity> withFilters(
            Long corretorIdFilter,
            Long tipoImovelId,
            String estado,
            String cidade,
            BigDecimal valorMin,
            BigDecimal valorMax,
            StatusImovel status,
            Disponibilidade disponibilidade,
            BigDecimal valorAluguelMin,
            BigDecimal valorAluguelMax
    ) {
        return (root, query, criteriaBuilder) -> {
            if (query != null && ImovelJpaEntity.class.equals(query.getResultType())) {
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();

            if (corretorIdFilter != null) {
                predicates.add(criteriaBuilder.equal(root.get("corretorId"), corretorIdFilter));
            }
            if (tipoImovelId != null) {
                predicates.add(criteriaBuilder.equal(root.get("tipoImovelId"), tipoImovelId));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (disponibilidade != null) {
                predicates.add(criteriaBuilder.equal(root.get("disponibilidade"), disponibilidade));
            }
            if (estado != null && !estado.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("estado")),
                        "%" + estado.toLowerCase() + "%"
                ));
            }
            if (cidade != null && !cidade.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("cidade")),
                        "%" + cidade.toLowerCase() + "%"
                ));
            }
            if (valorMin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("valor"), valorMin));
            }
            if (valorMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("valor"), valorMax));
            }
            if (valorAluguelMin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("valorAluguel"), valorAluguelMin));
            }
            if (valorAluguelMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("valorAluguel"), valorAluguelMax));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
