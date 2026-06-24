package br.com.gmob.cliente.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClienteAgendamentoResumoResponse(
        String nome,

        String telefone,

        String email
) {
}
