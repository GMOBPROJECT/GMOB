package br.com.gmob.imovel.infrastructure.persistence;

import br.com.gmob.imovel.domain.model.TipoImovel;
import br.com.gmob.imovel.domain.port.TipoImovelRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TipoImovelRepositoryAdapter implements TipoImovelRepositoryPort {

    private final SpringDataTipoImovelRepository repository;
    private final SpringDataImovelRepository imovelRepository;

    public TipoImovelRepositoryAdapter(
            SpringDataTipoImovelRepository repository,
            SpringDataImovelRepository imovelRepository
    ) {
        this.repository = repository;
        this.imovelRepository = imovelRepository;
    }

    @Override
    public TipoImovel save(TipoImovel tipoImovel) {
        return toDomain(repository.save(toEntity(tipoImovel)));
    }

    @Override
    public Optional<TipoImovel> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<TipoImovel> findByNomeTipo(String nomeTipo) {
        return repository.findByNomeTipo(nomeTipo).map(this::toDomain);
    }

    @Override
    public List<TipoImovel> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByNomeTipo(String nomeTipo) {
        return repository.existsByNomeTipo(nomeTipo);
    }

    @Override
    public long countImoveisByTipoImovelId(Long tipoImovelId) {
        return imovelRepository.countByTipoImovel_TipoImovelId(tipoImovelId);
    }

    private TipoImovel toDomain(TipoImovelJpaEntity entity) {
        return new TipoImovel(entity.getTipoImovelId(), entity.getNomeTipo());
    }

    private TipoImovelJpaEntity toEntity(TipoImovel tipoImovel) {
        TipoImovelJpaEntity entity = new TipoImovelJpaEntity();
        entity.setTipoImovelId(tipoImovel.id());
        entity.setNomeTipo(tipoImovel.nomeTipo());
        return entity;
    }
}
