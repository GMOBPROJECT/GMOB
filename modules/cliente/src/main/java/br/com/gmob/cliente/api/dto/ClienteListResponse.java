package br.com.gmob.cliente.api.dto;

import br.com.gmob.infra.dto.PaginationResponse;

import java.util.List;

public record ClienteListResponse(
        List<ClienteResponse> clientes,
        PaginationResponse pagination
) {
}
