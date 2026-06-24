package br.com.gmob.imovel.api.dto;

import br.com.gmob.infra.domain.enums.Disponibilidade;
import br.com.gmob.infra.domain.enums.StatusImovel;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ImovelResponse(
        @JsonProperty("imovel_id")
        Long imovelId,

        @JsonProperty("corretor_id")
        Long corretorId,

        @JsonProperty("tipo_imovel_id")
        Long tipoImovelId,

        StatusImovel status,

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

        String descricao,

        @JsonProperty("data_cadastro")
        Instant dataCadastro,

        CorretorResumoResponse corretor,

        List<ImagemImovelResponse> imagens
) {
}
