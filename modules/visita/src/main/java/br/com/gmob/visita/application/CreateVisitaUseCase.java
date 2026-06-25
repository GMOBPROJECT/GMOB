package br.com.gmob.visita.application;

import br.com.gmob.infra.domain.enums.StatusAgendamento;
import br.com.gmob.infra.exception.ConflictException;
import br.com.gmob.infra.security.AuthenticatedUser;
import br.com.gmob.visita.api.dto.CreateVisitaRequest;
import br.com.gmob.visita.api.dto.VisitaResponse;
import br.com.gmob.visita.api.mapper.VisitaMapper;
import br.com.gmob.visita.application.port.VisitaClientePort;
import br.com.gmob.visita.application.port.VisitaImovelPort;
import br.com.gmob.visita.domain.event.VisitaAgendadaEvent;
import br.com.gmob.visita.domain.model.Visita;
import br.com.gmob.visita.domain.port.VisitaRepositoryPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class CreateVisitaUseCase {

    private final VisitaRepositoryPort visitaRepository;
    private final VisitaImovelPort visitaImovelPort;
    private final VisitaClientePort visitaClientePort;
    private final ApplicationEventPublisher eventPublisher;

    public CreateVisitaUseCase(
            VisitaRepositoryPort visitaRepository,
            VisitaImovelPort visitaImovelPort,
            VisitaClientePort visitaClientePort,
            ApplicationEventPublisher eventPublisher
    ) {
        this.visitaRepository = visitaRepository;
        this.visitaImovelPort = visitaImovelPort;
        this.visitaClientePort = visitaClientePort;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public VisitaResponse execute(CreateVisitaRequest request, AuthenticatedUser currentUser) {
        VisitaAuthorization.validateRole(currentUser);

        LocalDate dataVisita = VisitaScheduleValidator.parseDate(request.dataVisita());
        LocalTime horaInicio = VisitaScheduleValidator.parseTime(request.horaInicio());
        LocalTime horaTermino = VisitaScheduleValidator.parseTime(request.horaTermino());
        VisitaScheduleValidator.validateFutureSchedule(dataVisita, horaInicio, horaTermino);

        VisitaImovelPort.ImovelVisitaView imovel =
                visitaImovelPort.findByIdForVisita(request.imovelId(), currentUser);
        VisitaClientePort.ClienteVisitaView cliente =
                visitaClientePort.findByIdForVisita(request.clienteId(), currentUser);

        if (visitaRepository.existsByClienteIdAndImovelIdAndDataVisita(
                request.clienteId(),
                request.imovelId(),
                dataVisita
        )) {
            throw new ConflictException(
                    "O mesmo cliente não pode agendar mais de uma visita ao mesmo imóvel no mesmo dia."
            );
        }

        Visita visita = visitaRepository.save(new Visita(
                null,
                currentUser.corretorId(),
                request.imovelId(),
                request.clienteId(),
                dataVisita,
                horaInicio,
                horaTermino,
                request.observacoes(),
                StatusAgendamento.AGENDADO,
                Instant.now()
        ));

        eventPublisher.publishEvent(new VisitaAgendadaEvent(
                visita.agendamentoId(),
                visita.corretorId(),
                visita.imovelId(),
                visita.clienteId(),
                visita.dataVisita(),
                visita.horaInicio(),
                visita.horaTermino(),
                visita.observacoes(),
                imovel.enderecoFormatado(),
                cliente.nome(),
                cliente.email(),
                currentUser.nomeCompleto()
        ));

        return VisitaMapper.toResponse(visita);
    }
}
