package br.com.gmob.visita.application;

import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ConflictException;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import br.com.gmob.visita.api.dto.UpdateVisitaRequest;
import br.com.gmob.visita.api.dto.VisitaResponse;
import br.com.gmob.visita.api.mapper.VisitaMapper;
import br.com.gmob.visita.domain.model.Visita;
import br.com.gmob.visita.domain.port.VisitaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class UpdateVisitaUseCase {

    private final VisitaRepositoryPort visitaRepository;

    public UpdateVisitaUseCase(VisitaRepositoryPort visitaRepository) {
        this.visitaRepository = visitaRepository;
    }

    @Transactional
    public VisitaResponse execute(Long id, UpdateVisitaRequest request, AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.CORRETOR && currentUser.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Acesso negado");
        }

        Visita existing = visitaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        if (currentUser.perfil() == Perfil.CORRETOR
                && !existing.corretorId().equals(currentUser.corretorId())) {
            throw new ResourceNotFoundException("Agendamento não encontrado");
        }

        Long imovelId = request.imovelId() != null ? request.imovelId() : existing.imovelId();
        Long clienteId = request.clienteId() != null ? request.clienteId() : existing.clienteId();
        LocalDate dataVisita = request.dataVisita() != null
                ? VisitaScheduleValidator.parseDate(request.dataVisita())
                : existing.dataVisita();
        LocalTime horaInicio = request.horaInicio() != null
                ? VisitaScheduleValidator.parseTime(request.horaInicio())
                : existing.horaInicio();
        LocalTime horaTermino = request.horaTermino() != null
                ? VisitaScheduleValidator.parseTime(request.horaTermino())
                : existing.horaTermino();

        if (visitaRepository.existsConflictOnUpdate(id, imovelId, clienteId, dataVisita, horaInicio)) {
            throw new ConflictException(
                    "Já existe um agendamento com o mesmo 'cliente', 'imóvel', 'data' e 'hora de início'."
            );
        }

        Visita updated = visitaRepository.save(new Visita(
                existing.agendamentoId(),
                existing.corretorId(),
                imovelId,
                clienteId,
                dataVisita,
                horaInicio,
                horaTermino,
                request.observacoes() != null ? request.observacoes() : existing.observacoes(),
                request.statusAgendamento() != null ? request.statusAgendamento() : existing.statusAgendamento(),
                existing.dataAgendamento()
        ));

        return VisitaMapper.toResponse(updated);
    }
}
