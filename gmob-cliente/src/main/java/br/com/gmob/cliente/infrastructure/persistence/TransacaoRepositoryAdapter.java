package br.com.gmob.cliente.infrastructure.persistence;

import br.com.gmob.cliente.domain.model.ClienteResumo;
import br.com.gmob.cliente.domain.model.ImovelResumo;
import br.com.gmob.cliente.domain.model.TransacaoImovel;
import br.com.gmob.cliente.domain.model.TransacaoImovelDetalhe;
import br.com.gmob.cliente.domain.port.TransacaoRepositoryPort;
import br.com.gmob.imovel.infrastructure.persistence.ImovelJpaEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TransacaoRepositoryAdapter implements TransacaoRepositoryPort {

    private final SpringDataTransacaoRepository repository;

    public TransacaoRepositoryAdapter(SpringDataTransacaoRepository repository) {
        this.repository = repository;
    }

    @Override
    public TransacaoImovel save(TransacaoImovel transacao) {
        return toDomain(repository.save(toEntity(transacao)));
    }

    @Override
    public Optional<TransacaoImovel> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<TransacaoImovelDetalhe> findDetalheById(Long id) {
        return repository.findByTransacaoId(id).map(this::toDetalhe);
    }

    @Override
    public List<TransacaoImovelDetalhe> findAllByCorretorId(Long corretorId, int page, int limit) {
        return repository.findByCorretorIdOrderByDataTransacaoDesc(
                corretorId,
                PageRequest.of(page - 1, limit)
        ).stream().map(this::toDetalhe).toList();
    }

    @Override
    public long countByCorretorId(Long corretorId) {
        return repository.countByCorretorId(corretorId);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private TransacaoImovel toDomain(TransacaoImovelJpaEntity entity) {
        return new TransacaoImovel(
                entity.getTransacaoId(),
                entity.getImovelId(),
                entity.getClienteId(),
                entity.getCorretorId(),
                entity.getTipoTransacao(),
                entity.getDataTransacao()
        );
    }

    private TransacaoImovelDetalhe toDetalhe(TransacaoImovelJpaEntity entity) {
        ImovelResumo imovelResumo = null;
        if (entity.getImovel() != null) {
            ImovelJpaEntity imovel = entity.getImovel();
            imovelResumo = new ImovelResumo(
                    imovel.getImovelId(),
                    imovel.getRua(),
                    imovel.getNumero(),
                    imovel.getCidade(),
                    imovel.getValor(),
                    imovel.getValorAluguel()
            );
        }

        ClienteResumo clienteResumo = null;
        if (entity.getCliente() != null) {
            ClienteJpaEntity cliente = entity.getCliente();
            clienteResumo = new ClienteResumo(cliente.getClienteId(), cliente.getNome());
        }

        return new TransacaoImovelDetalhe(
                entity.getTransacaoId(),
                entity.getImovelId(),
                entity.getClienteId(),
                entity.getCorretorId(),
                entity.getTipoTransacao(),
                entity.getDataTransacao(),
                imovelResumo,
                clienteResumo
        );
    }

    private TransacaoImovelJpaEntity toEntity(TransacaoImovel transacao) {
        TransacaoImovelJpaEntity entity = new TransacaoImovelJpaEntity();
        entity.setTransacaoId(transacao.id());
        entity.setImovelId(transacao.imovelId());
        entity.setClienteId(transacao.clienteId());
        entity.setCorretorId(transacao.corretorId());
        entity.setTipoTransacao(transacao.tipoTransacao());
        entity.setDataTransacao(transacao.dataTransacao());
        return entity;
    }
}
