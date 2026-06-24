package br.com.gmob.imovel.api.dto;

import br.com.gmob.infra.domain.enums.Disponibilidade;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateImovelRequest(
        @JsonProperty("tipo_imovel_id")
        @NotNull(message = "O tipo do imóvel é obrigatório.")
        Long tipoImovelId,

        @JsonProperty("valor_aluguel")
        BigDecimal valorAluguel,

        @NotNull(message = "A disponibilidade é obrigatória.")
        Disponibilidade disponibilidade,

        @NotBlank
        String estado,

        @NotBlank
        String cidade,

        @NotBlank
        String rua,

        @NotBlank
        String numero,

        String complemento,

        @NotNull
        BigDecimal valor,

        @NotNull
        BigDecimal area,

        @JsonProperty("numero_comodos")
        @NotNull
        Integer numeroComodos,

        String descricao
) {
}
