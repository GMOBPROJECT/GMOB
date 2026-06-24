package br.com.gmob.cliente.api.dto;

import br.com.gmob.infra.domain.enums.Disponibilidade;
import br.com.gmob.infra.domain.enums.StatusImovel;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

public record ImovelFullResponse(
        @JsonProperty("imovel_id")
        Long imovelId,

        @JsonProperty("corretor_id")
        Long corretorId,

        @JsonProperty("tipo_imovel_id")
        Long tipoImovelId,

        StatusImovel status,

        Disponibilidade disponibilidade,

        @JsonProperty("valor_aluguel")
        BigDecimal valorAluguel,

        String estado,

        String cidade,

        String rua,

        String numero,

        String complemento,

        BigDecimal valor,

        BigDecimal area,

        @JsonProperty("numero_comodos")
        Integer numeroComodos,

        String descricao,

        @JsonProperty("data_cadastro")
        Instant dataCadastro
) {
}
