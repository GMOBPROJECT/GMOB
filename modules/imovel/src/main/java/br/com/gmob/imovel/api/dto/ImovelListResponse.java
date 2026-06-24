package br.com.gmob.imovel.api.dto;

import java.util.List;

public record ImovelListResponse(
        List<ImovelResponse> data,
        long total,
        int page,
        int lastPage
) {
}
