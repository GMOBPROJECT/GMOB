package br.com.gmob.imovel.domain.port;

import br.com.gmob.imovel.domain.model.TipoImovel;

import java.util.List;
import java.util.Optional;

public interface TipoImovelRepositoryPort {

    TipoImovel save(TipoImovel tipoImovel);

    Optional<TipoImovel> findById(Long id);

    Optional<TipoImovel> findByNomeTipo(String nomeTipo);

    List<TipoImovel> findAll();

    void delete(Long id);

    boolean existsByNomeTipo(String nomeTipo);

    long countImoveisByTipoImovelId(Long tipoImovelId);
}
