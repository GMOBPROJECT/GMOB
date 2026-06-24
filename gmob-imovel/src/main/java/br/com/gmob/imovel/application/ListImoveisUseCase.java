package br.com.gmob.imovel.application;

import br.com.gmob.imovel.api.dto.ImovelListResponse;
import br.com.gmob.imovel.api.dto.ImovelResponse;
import br.com.gmob.imovel.api.mapper.ImovelMapper;
import br.com.gmob.imovel.domain.model.Imovel;
import br.com.gmob.imovel.domain.port.ImovelRepositoryPort;
import br.com.gmob.infra.domain.enums.Disponibilidade;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.domain.enums.StatusImovel;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ListImoveisUseCase {

    private final ImovelRepositoryPort imovelRepository;

    public ListImoveisUseCase(ImovelRepositoryPort imovelRepository) {
        this.imovelRepository = imovelRepository;
    }

    public ImovelListResponse execute(
            AuthenticatedUser currentUser,
            int page,
            int limit,
            Long tipo,
            String estado,
            String cidade,
            BigDecimal valorMin,
            BigDecimal valorMax,
            StatusImovel status,
            Disponibilidade disponibilidade,
            BigDecimal valorAluguelMin,
            BigDecimal valorAluguelMax
    ) {
        Long corretorFilter = currentUser.perfil() == Perfil.CORRETOR
                ? currentUser.corretorId()
                : null;

        List<Imovel> imoveis = imovelRepository.findAll(
                page,
                limit,
                corretorFilter,
                tipo,
                estado,
                cidade,
                valorMin,
                valorMax,
                status,
                disponibilidade,
                valorAluguelMin,
                valorAluguelMax
        );

        long total = imovelRepository.count(
                corretorFilter,
                tipo,
                estado,
                cidade,
                valorMin,
                valorMax,
                status,
                disponibilidade,
                valorAluguelMin,
                valorAluguelMax
        );

        List<ImovelResponse> data = imoveis.stream().map(ImovelMapper::toResponse).toList();
        int lastPage = limit > 0 ? (int) Math.ceil((double) total / limit) : 0;

        return new ImovelListResponse(data, total, page, lastPage);
    }
}
