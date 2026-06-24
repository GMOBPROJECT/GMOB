package br.com.gmob.imovel.domain.model;

import br.com.gmob.infra.domain.enums.Disponibilidade;
import br.com.gmob.infra.domain.enums.StatusImovel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record Imovel(
        Long id,
        Long corretorId,
        Long tipoImovelId,
        StatusImovel status,
        BigDecimal valorAluguel,
        Disponibilidade disponibilidade,
        String estado,
        String cidade,
        String rua,
        String numero,
        String complemento,
        BigDecimal valor,
        BigDecimal area,
        Integer numeroComodos,
        String descricao,
        Instant dataCadastro,
        List<ImagemImovel> imagens,
        String corretorNomeCompleto
) {
    public Imovel withoutImagens() {
        return new Imovel(
                id, corretorId, tipoImovelId, status, valorAluguel, disponibilidade,
                estado, cidade, rua, numero, complemento, valor, area, numeroComodos,
                descricao, dataCadastro, List.of(), corretorNomeCompleto
        );
    }

    public Imovel withImagens(List<ImagemImovel> imagens) {
        return new Imovel(
                id, corretorId, tipoImovelId, status, valorAluguel, disponibilidade,
                estado, cidade, rua, numero, complemento, valor, area, numeroComodos,
                descricao, dataCadastro, imagens, corretorNomeCompleto
        );
    }
}
