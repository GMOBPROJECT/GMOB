package br.com.gmob.cliente.domain.model;

import br.com.gmob.infra.domain.enums.TipoTransacao;

import java.time.Instant;

public record TransacaoImovel(
        Long id,
        Long imovelId,
        Long clienteId,
        Long corretorId,
        TipoTransacao tipoTransacao,
        Instant dataTransacao
) {
}
