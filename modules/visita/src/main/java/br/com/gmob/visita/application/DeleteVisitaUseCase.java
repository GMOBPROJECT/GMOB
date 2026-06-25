package br.com.gmob.visita.application;

import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import br.com.gmob.visita.api.dto.VisitaResponse;
import br.com.gmob.visita.api.mapper.VisitaMapper;
import br.com.gmob.visita.domain.model.Visita;
import br.com.gmob.visita.domain.port.VisitaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteVisitaUseCase {

    private final VisitaRepositoryPort visitaRepository;

    public DeleteVisitaUseCase(VisitaRepositoryPort visitaRepository) {
        this.visitaRepository = visitaRepository;
    }

    @Transactional
    public VisitaResponse execute(Long id, AuthenticatedUser currentUser) {
        VisitaAuthorization.validateRole(currentUser);

        Visita visita = visitaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado ou acesso negado"));

        if (currentUser.perfil() == br.com.gmob.infra.domain.enums.Perfil.CORRETOR
                && !visita.corretorId().equals(currentUser.corretorId())) {
            throw new ResourceNotFoundException("Agendamento não encontrado ou acesso negado");
        }

        visitaRepository.deleteById(id);
        return VisitaMapper.toResponse(visita);
    }
}
