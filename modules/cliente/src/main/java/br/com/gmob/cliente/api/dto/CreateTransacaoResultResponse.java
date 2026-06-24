package br.com.gmob.cliente.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateTransacaoResultResponse(
        TransacaoResponse transacao,

        @JsonProperty("agendamentosPendentes")
        List<AgendamentoPendenteResponse> agendamentosPendentes
) {
}
