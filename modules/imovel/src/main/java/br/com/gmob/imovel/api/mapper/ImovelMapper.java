package br.com.gmob.imovel.api.mapper;

import br.com.gmob.imovel.api.dto.CorretorResumoResponse;
import br.com.gmob.imovel.api.dto.ImagemImovelResponse;
import br.com.gmob.imovel.api.dto.ImovelResponse;
import br.com.gmob.imovel.api.dto.TipoImovelResponse;
import br.com.gmob.imovel.domain.model.ImagemImovel;
import br.com.gmob.imovel.domain.model.Imovel;
import br.com.gmob.imovel.domain.model.TipoImovel;

import java.util.List;

public final class ImovelMapper {

    private ImovelMapper() {
    }

    public static ImovelResponse toResponse(Imovel imovel) {
        List<ImagemImovelResponse> imagens = imovel.imagens() == null
                ? List.of()
                : imovel.imagens().stream().map(ImovelMapper::toImagemResponse).toList();

        CorretorResumoResponse corretor = imovel.corretorNomeCompleto() != null
                ? new CorretorResumoResponse(imovel.corretorNomeCompleto())
                : null;

        return new ImovelResponse(
                imovel.id(),
                imovel.corretorId(),
                imovel.tipoImovelId(),
                imovel.status(),
                imovel.valorAluguel(),
                imovel.disponibilidade(),
                imovel.estado(),
                imovel.cidade(),
                imovel.rua(),
                imovel.numero(),
                imovel.complemento(),
                imovel.valor(),
                imovel.area(),
                imovel.numeroComodos(),
                imovel.descricao(),
                imovel.dataCadastro(),
                corretor,
                imagens
        );
    }

    public static ImagemImovelResponse toImagemResponse(ImagemImovel imagem) {
        return new ImagemImovelResponse(imagem.id(), imagem.url(), imagem.imovelId());
    }
}
