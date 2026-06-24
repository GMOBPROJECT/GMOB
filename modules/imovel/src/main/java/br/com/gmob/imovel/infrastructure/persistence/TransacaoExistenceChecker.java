package br.com.gmob.imovel.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class TransacaoExistenceChecker {

    private final EntityManager entityManager;

    public TransacaoExistenceChecker(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean existsByImovelId(Long imovelId) {
        Number count = (Number) entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM transacoes_imoveis WHERE imovel_id = :imovelId"
                )
                .setParameter("imovelId", imovelId)
                .getSingleResult();
        return count.longValue() > 0;
    }
}
