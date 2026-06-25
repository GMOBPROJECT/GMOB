package br.com.gmob.visita.infrastructure.notification;

import br.com.gmob.visita.domain.event.VisitaAgendadaEvent;

public interface VisitaNotificationService {

    void enviarConfirmacaoVisita(VisitaAgendadaEvent event);
}
