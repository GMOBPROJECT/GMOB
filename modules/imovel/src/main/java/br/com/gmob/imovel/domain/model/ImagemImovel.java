package br.com.gmob.imovel.domain.model;

public record ImagemImovel(
        Long id,
        String url,
        Long imovelId
) {
}
