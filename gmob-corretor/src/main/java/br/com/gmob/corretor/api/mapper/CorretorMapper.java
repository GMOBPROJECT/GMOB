package br.com.gmob.corretor.api.mapper;

import br.com.gmob.corretor.api.dto.CorretorListResponse;
import br.com.gmob.corretor.api.dto.CorretorResponse;
import br.com.gmob.corretor.domain.model.Corretor;
import br.com.gmob.infra.dto.PaginationResponse;

import java.util.List;

public final class CorretorMapper {

    private CorretorMapper() {
    }

    public static CorretorResponse toResponse(Corretor corretor) {
        return new CorretorResponse(
                corretor.id(),
                corretor.nomeCompleto(),
                corretor.email(),
                corretor.telefone(),
                corretor.cpf(),
                corretor.perfil(),
                corretor.dataCadastro()
        );
    }

    public static List<CorretorResponse> toResponseList(List<Corretor> corretores) {
        return corretores.stream()
                .map(CorretorMapper::toResponse)
                .toList();
    }

    public static CorretorListResponse toListResponse(List<Corretor> corretores, int page, int limit, long total) {
        int totalPages = limit > 0 ? (int) Math.ceil((double) total / limit) : 0;
        return new CorretorListResponse(
                toResponseList(corretores),
                new PaginationResponse(page, limit, total, totalPages)
        );
    }
}
