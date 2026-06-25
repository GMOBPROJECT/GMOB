package br.com.gmob.visita.infrastructure.event;

import br.com.gmob.infra.event.ImovelRemovidoEvent;
import br.com.gmob.visita.domain.port.VisitaRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class VisitaCleanupListener {

    private final VisitaRepositoryPort visitaRepository;

    public VisitaCleanupListener(VisitaRepositoryPort visitaRepository) {
        this.visitaRepository = visitaRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onImovelRemovido(ImovelRemovidoEvent event) {
        visitaRepository.deleteByImovelId(event.imovelId(), event.corretorId(), event.isAdmin());
    }
}
