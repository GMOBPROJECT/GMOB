package br.com.gmob.imovel.infrastructure.persistence;

import br.com.gmob.imovel.domain.model.ImagemImovel;
import br.com.gmob.imovel.domain.model.Imovel;
import br.com.gmob.imovel.domain.port.ImovelRepositoryPort;
import br.com.gmob.infra.domain.enums.Disponibilidade;
import br.com.gmob.infra.domain.enums.StatusImovel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class ImovelRepositoryAdapter implements ImovelRepositoryPort {

    private final SpringDataImovelRepository repository;
    private final TransacaoExistenceChecker transacaoExistenceChecker;

    public ImovelRepositoryAdapter(
            SpringDataImovelRepository repository,
            TransacaoExistenceChecker transacaoExistenceChecker
    ) {
        this.repository = repository;
        this.transacaoExistenceChecker = transacaoExistenceChecker;
    }

    @Override
    public Imovel save(Imovel imovel) {
        return toDomain(repository.save(toEntity(imovel)));
    }

    @Override
    public Optional<Imovel> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Imovel> findByIdWithImagens(Long id) {
        return repository.findByIdWithImagens(id).map(this::toDomainWithRelations);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByAddress(String rua, String numero, String complemento, String cidade, String estado) {
        return repository.existsByAddress(rua, numero, complemento, cidade, estado);
    }

    @Override
    public boolean existsTransacaoByImovelId(Long imovelId) {
        return transacaoExistenceChecker.existsByImovelId(imovelId);
    }

    @Override
    public List<Imovel> findAll(
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
    ) {
        Specification<ImovelJpaEntity> spec = ImovelSpecifications.withFilters(
                corretorIdFilter, tipoImovelId, estado, cidade,
                valorMin, valorMax, status, disponibilidade, valorAluguelMin, valorAluguelMax
        );

        return repository.findAll(spec, PageRequest.of(page - 1, limit))
                .stream()
                .map(this::toDomainWithRelations)
                .toList();
    }

    @Override
    public long count(
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
    ) {
        Specification<ImovelJpaEntity> spec = ImovelSpecifications.withFilters(
                corretorIdFilter, tipoImovelId, estado, cidade,
                valorMin, valorMax, status, disponibilidade, valorAluguelMin, valorAluguelMax
        );
        return repository.count(spec);
    }

    private Imovel toDomain(ImovelJpaEntity entity) {
        return new Imovel(
                entity.getImovelId(),
                entity.getCorretorId(),
                entity.getTipoImovelId(),
                entity.getStatus(),
                entity.getValorAluguel(),
                entity.getDisponibilidade(),
                entity.getEstado(),
                entity.getCidade(),
                entity.getRua(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getValor(),
                entity.getArea(),
                entity.getNumeroComodos(),
                entity.getDescricao(),
                entity.getDataCadastro(),
                List.of(),
                null
        );
    }

    private Imovel toDomainWithRelations(ImovelJpaEntity entity) {
        List<ImagemImovel> imagens = entity.getImagens() == null
                ? List.of()
                : entity.getImagens().stream()
                .map(img -> new ImagemImovel(img.getImagemId(), img.getUrl(), img.getImovelId()))
                .toList();

        String corretorNome = entity.getCorretor() != null
                ? entity.getCorretor().getNomeCompleto()
                : null;

        return new Imovel(
                entity.getImovelId(),
                entity.getCorretorId(),
                entity.getTipoImovelId(),
                entity.getStatus(),
                entity.getValorAluguel(),
                entity.getDisponibilidade(),
                entity.getEstado(),
                entity.getCidade(),
                entity.getRua(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getValor(),
                entity.getArea(),
                entity.getNumeroComodos(),
                entity.getDescricao(),
                entity.getDataCadastro(),
                imagens,
                corretorNome
        );
    }

    private ImovelJpaEntity toEntity(Imovel imovel) {
        ImovelJpaEntity entity = new ImovelJpaEntity();
        entity.setImovelId(imovel.id());
        entity.setCorretorId(imovel.corretorId());
        entity.setStatus(imovel.status());
        entity.setValorAluguel(imovel.valorAluguel());
        entity.setDisponibilidade(imovel.disponibilidade());
        entity.setEstado(imovel.estado());
        entity.setCidade(imovel.cidade());
        entity.setRua(imovel.rua());
        entity.setNumero(imovel.numero());
        entity.setComplemento(imovel.complemento());
        entity.setValor(imovel.valor());
        entity.setArea(imovel.area());
        entity.setNumeroComodos(imovel.numeroComodos());
        entity.setDescricao(imovel.descricao());
        entity.setDataCadastro(imovel.dataCadastro());

        if (imovel.tipoImovelId() != null) {
            TipoImovelJpaEntity tipoImovel = new TipoImovelJpaEntity();
            tipoImovel.setTipoImovelId(imovel.tipoImovelId());
            entity.setTipoImovel(tipoImovel);
        }

        return entity;
    }
}
