package br.com.gmob.visita.infrastructure.event;

import br.com.gmob.visita.domain.event.VisitaAgendadaEvent;
import br.com.gmob.visita.infrastructure.notification.VisitaNotificationService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class VisitaNotificacaoListener {

    private final VisitaNotificationService notificationService;

    public VisitaNotificacaoListener(VisitaNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Async("notificationExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onVisitaAgendada(VisitaAgendadaEvent event) {
        notificationService.enviarConfirmacaoVisita(event);
    }
}
