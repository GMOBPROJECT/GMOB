package br.com.gmob.imovel.api.dto;

import br.com.gmob.infra.domain.enums.Disponibilidade;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record UpdateImovelRequest(
        @JsonProperty("tipo_imovel_id")
        Long tipoImovelId,

        @JsonProperty("valor_aluguel")
        BigDecimal valorAluguel,

        Disponibilidade disponibilidade,

        String estado,

        String cidade,

        String rua,

        String numero,

        String complemento,

        BigDecimal valor,

        BigDecimal area,

        @JsonProperty("numero_comodos")
        Integer numeroComodos,

        String descricao
) {
}
