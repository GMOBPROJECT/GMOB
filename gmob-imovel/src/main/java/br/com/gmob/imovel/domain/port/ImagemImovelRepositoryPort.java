package br.com.gmob.imovel.domain.port;

import br.com.gmob.imovel.domain.model.ImagemImovel;

import java.util.List;

public interface ImagemImovelRepositoryPort {

    List<ImagemImovel> saveAll(List<ImagemImovel> imagens);

    void deleteByImovelId(Long imovelId);

    List<ImagemImovel> findByImovelId(Long imovelId);
}
