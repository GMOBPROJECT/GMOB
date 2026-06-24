package br.com.gmob.infra.dto;

public record PaginationResponse(
        int page,
        int limit,
        long total,
        int totalPages
) {
}
