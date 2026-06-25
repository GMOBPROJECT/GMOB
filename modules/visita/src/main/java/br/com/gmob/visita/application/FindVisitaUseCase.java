package br.com.gmob.visita.application;

import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import br.com.gmob.visita.api.dto.VisitaResponse;
import br.com.gmob.visita.api.mapper.VisitaMapper;
import br.com.gmob.visita.domain.model.Visita;
import br.com.gmob.visita.domain.port.VisitaRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class FindVisitaUseCase {

    private final VisitaRepositoryPort visitaRepository;

    public FindVisitaUseCase(VisitaRepositoryPort visitaRepository) {
        this.visitaRepository = visitaRepository;
    }

    public VisitaResponse execute(Long id, AuthenticatedUser currentUser) {
        VisitaAuthorization.validateRole(currentUser);

        Visita visita = visitaRepository.findById(id)
                .orElseThrow(() -> new ForbiddenException("Agendamento não encontrado"));

        VisitaAuthorization.validateOwnership(visita, currentUser);

        return VisitaMapper.toResponse(visita);
    }
}
