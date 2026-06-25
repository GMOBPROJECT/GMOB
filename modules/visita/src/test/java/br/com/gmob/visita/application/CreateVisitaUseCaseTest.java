package br.com.gmob.visita.application;

import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.domain.enums.StatusAgendamento;
import br.com.gmob.infra.exception.ConflictException;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import br.com.gmob.visita.api.dto.CreateVisitaRequest;
import br.com.gmob.visita.application.port.VisitaClientePort;
import br.com.gmob.visita.application.port.VisitaImovelPort;
import br.com.gmob.visita.domain.event.VisitaAgendadaEvent;
import br.com.gmob.visita.domain.model.Visita;
import br.com.gmob.visita.domain.port.VisitaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateVisitaUseCaseTest {

    @Mock
    private VisitaRepositoryPort visitaRepository;

    @Mock
    private VisitaImovelPort visitaImovelPort;

    @Mock
    private VisitaClientePort visitaClientePort;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private CreateVisitaUseCase useCase;

    private final AuthenticatedUser corretor = new AuthenticatedUser(
            1L, "Corretor Teste", "corretor@test.com", "(82) 99999-9999", "111.111.111-11",
            Perfil.CORRETOR, Instant.now()
    );

    @BeforeEach
    void setUp() {
        useCase = new CreateVisitaUseCase(
                visitaRepository,
                visitaImovelPort,
                visitaClientePort,
                eventPublisher
        );
    }

    @Test
    void deveCriarVisitaEPublicarEvento() {
        LocalDate dataFutura = LocalDate.now().plusDays(5);
        CreateVisitaRequest request = new CreateVisitaRequest(
                10L, 20L, dataFutura.toString(), "10:00", "11:00", "Observação teste"
        );

        when(visitaImovelPort.findByIdForVisita(10L, corretor))
                .thenReturn(new VisitaImovelPort.ImovelVisitaView(10L, 1L, "Rua A, 1 - Maceió/AL"));
        when(visitaClientePort.findByIdForVisita(20L, corretor))
                .thenReturn(new VisitaClientePort.ClienteVisitaView(20L, "Cliente", "cliente@test.com", "(82) 98888-8888"));
        when(visitaRepository.existsByClienteIdAndImovelIdAndDataVisita(20L, 10L, dataFutura)).thenReturn(false);
        when(visitaRepository.save(any())).thenAnswer(invocation -> {
            Visita visita = invocation.getArgument(0);
            return new Visita(
                    99L,
                    visita.corretorId(),
                    visita.imovelId(),
                    visita.clienteId(),
                    visita.dataVisita(),
                    visita.horaInicio(),
                    visita.horaTermino(),
                    visita.observacoes(),
                    visita.statusAgendamento(),
                    visita.dataAgendamento()
            );
        });

        var response = useCase.execute(request, corretor);

        assertEquals(99L, response.agendamentoId());
        assertEquals(StatusAgendamento.AGENDADO, response.statusAgendamento());

        ArgumentCaptor<VisitaAgendadaEvent> eventCaptor = ArgumentCaptor.forClass(VisitaAgendadaEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertEquals("cliente@test.com", eventCaptor.getValue().clienteEmail());
    }

    @Test
    void deveRejeitarDataPassada() {
        CreateVisitaRequest request = new CreateVisitaRequest(
                10L, 20L, LocalDate.now().minusDays(1).toString(), "10:00", "11:00", "Obs"
        );

        assertThrows(ForbiddenException.class, () -> useCase.execute(request, corretor));
    }

    @Test
    void deveRejeitarConflitoClienteImovelDia() {
        LocalDate dataFutura = LocalDate.now().plusDays(3);
        CreateVisitaRequest request = new CreateVisitaRequest(
                10L, 20L, dataFutura.toString(), "10:00", "11:00", "Obs"
        );

        when(visitaImovelPort.findByIdForVisita(10L, corretor))
                .thenReturn(new VisitaImovelPort.ImovelVisitaView(10L, 1L, "Endereço"));
        when(visitaClientePort.findByIdForVisita(20L, corretor))
                .thenReturn(new VisitaClientePort.ClienteVisitaView(20L, "Cliente", "c@t.com", "tel"));
        when(visitaRepository.existsByClienteIdAndImovelIdAndDataVisita(20L, 10L, dataFutura)).thenReturn(true);

        assertThrows(ConflictException.class, () -> useCase.execute(request, corretor));
    }
}
