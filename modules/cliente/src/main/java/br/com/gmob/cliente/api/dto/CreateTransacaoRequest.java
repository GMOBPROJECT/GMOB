package br.com.gmob.cliente.api.dto;

import br.com.gmob.infra.domain.enums.TipoTransacao;
import br.com.gmob.infra.validation.ValidationPatterns;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateTransacaoRequest(
        @JsonProperty("imovel_id")
        @NotNull(message = "O ID do imóvel é obrigatório.")
        Long imovelId,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = ValidationPatterns.CPF_PATTERN,
                message = "CPF deve estar no formato XXX.XXX.XXX-XX")
        String cpf,

        @JsonProperty("tipo_transacao")
        @NotNull(message = "O tipo de transação é obrigatório.")
        TipoTransacao tipoTransacao
) {
}
