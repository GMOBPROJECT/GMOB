package br.com.gmob.imovel.infrastructure.persistence;

import br.com.gmob.imovel.domain.model.ImagemImovel;
import br.com.gmob.imovel.domain.port.ImagemImovelRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ImagemImovelRepositoryAdapter implements ImagemImovelRepositoryPort {

    private final SpringDataImagemImovelRepository repository;

    public ImagemImovelRepositoryAdapter(SpringDataImagemImovelRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ImagemImovel> saveAll(List<ImagemImovel> imagens) {
        List<ImagemImovelJpaEntity> entities = imagens.stream().map(this::toEntity).toList();
        return repository.saveAll(entities).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteByImovelId(Long imovelId) {
        repository.deleteByImovel_ImovelId(imovelId);
    }

    @Override
    public List<ImagemImovel> findByImovelId(Long imovelId) {
        return repository.findByImovel_ImovelId(imovelId).stream().map(this::toDomain).toList();
    }

    private ImagemImovel toDomain(ImagemImovelJpaEntity entity) {
        return new ImagemImovel(entity.getImagemId(), entity.getUrl(), entity.getImovelId());
    }

    private ImagemImovelJpaEntity toEntity(ImagemImovel imagem) {
        ImagemImovelJpaEntity entity = new ImagemImovelJpaEntity();
        entity.setImagemId(imagem.id());
        entity.setUrl(imagem.url());

        ImovelJpaEntity imovel = new ImovelJpaEntity();
        imovel.setImovelId(imagem.imovelId());
        entity.setImovel(imovel);

        return entity;
    }
}
