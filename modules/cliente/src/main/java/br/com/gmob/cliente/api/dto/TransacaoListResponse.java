package br.com.gmob.cliente.api.dto;

import br.com.gmob.infra.dto.PaginationResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TransacaoListResponse(
        @JsonProperty("transacoesComDetalhes")
        List<TransacaoDetalheResponse> transacoesComDetalhes,

        PaginationResponse pagination
) {
}
