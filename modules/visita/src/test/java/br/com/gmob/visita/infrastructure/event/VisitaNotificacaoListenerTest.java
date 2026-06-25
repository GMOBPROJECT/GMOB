package br.com.gmob.visita.infrastructure.event;

import br.com.gmob.visita.domain.event.VisitaAgendadaEvent;
import br.com.gmob.visita.infrastructure.notification.VisitaNotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VisitaNotificacaoListenerTest {

    @Mock
    private VisitaNotificationService notificationService;

    @InjectMocks
    private VisitaNotificacaoListener listener;

    @Test
    void deveEnviarNotificacaoAoReceberEvento() {
        VisitaAgendadaEvent event = new VisitaAgendadaEvent(
                1L, 2L, 3L, 4L,
                LocalDate.of(2026, 7, 1),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                "Obs",
                "Rua Teste, 1 - Maceió/AL",
                "Cliente",
                "cliente@test.com",
                "Corretor"
        );

        listener.onVisitaAgendada(event);

        verify(notificationService).enviarConfirmacaoVisita(event);
    }
}
