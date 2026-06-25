package br.com.gmob.infra.event;

public record ImovelRemovidoEvent(
        Long imovelId,
        Long corretorId,
        boolean isAdmin
) {
}
