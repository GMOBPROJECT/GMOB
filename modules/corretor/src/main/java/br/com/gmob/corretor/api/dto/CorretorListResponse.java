package br.com.gmob.corretor.api.dto;

import br.com.gmob.infra.dto.PaginationResponse;

import java.util.List;

public record CorretorListResponse(
        List<CorretorResponse> corretores,
        PaginationResponse pagination
) {
}
