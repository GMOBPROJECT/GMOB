package br.com.gmob.imovel.domain.port;

import br.com.gmob.imovel.domain.model.Imovel;
import br.com.gmob.infra.domain.enums.Disponibilidade;
import br.com.gmob.infra.domain.enums.StatusImovel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ImovelRepositoryPort {

    Imovel save(Imovel imovel);

    Optional<Imovel> findById(Long id);

    Optional<Imovel> findByIdWithImagens(Long id);

    void delete(Long id);

    boolean existsByAddress(String rua, String numero, String complemento, String cidade, String estado);

    boolean existsTransacaoByImovelId(Long imovelId);

    List<Imovel> findAll(
            int page,
            int limit,
            Long corretorIdFilter,
            Long tipoImovelId,
            String estado,
            String cidade,
            BigDecimal valorMin,
            BigDecimal valorMax,
            StatusImovel status,
            Disponibilidade disponibilidade,
            BigDecimal valorAluguelMin,
            BigDecimal valorAluguelMax
    );

    long count(
            Long corretorIdFilter,
            Long tipoImovelId,
            String estado,
            String cidade,
            BigDecimal valorMin,
            BigDecimal valorMax,
            StatusImovel status,
            Disponibilidade disponibilidade,
            BigDecimal valorAluguelMin,
            BigDecimal valorAluguelMax
    );
}
