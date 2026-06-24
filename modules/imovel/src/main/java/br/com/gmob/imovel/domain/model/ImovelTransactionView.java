package br.com.gmob.imovel.domain.model;

import br.com.gmob.infra.domain.enums.StatusImovel;

public record ImovelTransactionView(
        Long id,
        Long corretorId,
        StatusImovel status
) {
}
