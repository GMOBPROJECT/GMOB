package br.com.gmob.imovel.api.mapper;

import br.com.gmob.imovel.api.dto.TipoImovelResponse;
import br.com.gmob.imovel.domain.model.TipoImovel;

public final class TipoImovelMapper {

    private TipoImovelMapper() {
    }

    public static TipoImovelResponse toResponse(TipoImovel tipoImovel) {
        return new TipoImovelResponse(tipoImovel.id(), tipoImovel.nomeTipo());
    }
}
