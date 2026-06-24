package br.com.gmob.cliente.domain.port;

import br.com.gmob.cliente.domain.model.TransacaoImovel;
import br.com.gmob.cliente.domain.model.TransacaoImovelDetalhe;

import java.util.List;
import java.util.Optional;

public interface TransacaoRepositoryPort {

    TransacaoImovel save(TransacaoImovel transacao);

    Optional<TransacaoImovel> findById(Long id);

    Optional<TransacaoImovelDetalhe> findDetalheById(Long id);

    List<TransacaoImovelDetalhe> findAllByCorretorId(Long corretorId, int page, int limit);

    long countByCorretorId(Long corretorId);

    void delete(Long id);
}
