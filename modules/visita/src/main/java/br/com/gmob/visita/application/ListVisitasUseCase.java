package br.com.gmob.visita.application;

import br.com.gmob.infra.domain.enums.StatusAgendamento;
import br.com.gmob.infra.security.AuthenticatedUser;
import br.com.gmob.visita.api.dto.VisitaListResponse;
import br.com.gmob.visita.api.dto.VisitaResponse;
import br.com.gmob.visita.api.mapper.VisitaMapper;
import br.com.gmob.visita.domain.port.VisitaRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ListVisitasUseCase {

    private final VisitaRepositoryPort visitaRepository;

    public ListVisitasUseCase(VisitaRepositoryPort visitaRepository) {
        this.visitaRepository = visitaRepository;
    }

    public VisitaListResponse execute(
            AuthenticatedUser currentUser,
            Long imovelId,
            Long clienteId,
            LocalDate data,
            StatusAgendamento status,
            int page,
            int limit
    ) {
        VisitaAuthorization.validateRole(currentUser);

        Long corretorFilter = VisitaAuthorization.corretorFilter(currentUser);
        List<VisitaResponse> dataList = visitaRepository.findAll(
                        corretorFilter,
                        imovelId,
                        clienteId,
                        data,
                        status,
                        page,
                        limit
                )
                .stream()
                .map(VisitaMapper::toResponse)
                .toList();

        long total = visitaRepository.count(corretorFilter, imovelId, clienteId, data, status);
        int totalPages = limit > 0 ? (int) Math.ceil((double) total / limit) : 0;

        return new VisitaListResponse(dataList, total, page, limit, totalPages);
    }
}
