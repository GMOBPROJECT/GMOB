package br.com.gmob.cliente.domain.model;

import java.math.BigDecimal;

public record ImovelResumo(
        Long id,
        String rua,
        String numero,
        String cidade,
        BigDecimal valor,
        BigDecimal valorAluguel
) {
}
