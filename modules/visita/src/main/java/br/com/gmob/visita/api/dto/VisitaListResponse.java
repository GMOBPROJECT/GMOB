package br.com.gmob.visita.api.dto;

import java.util.List;

public record VisitaListResponse(
        List<VisitaResponse> data,
        long total,
        int page,
        int limit,
        int totalPages
) {
}
