package br.com.gmob.imovel.application;

import br.com.gmob.imovel.api.dto.TipoImovelResponse;
import br.com.gmob.imovel.api.mapper.TipoImovelMapper;
import br.com.gmob.imovel.domain.port.TipoImovelRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListTiposImovelUseCase {

    private final TipoImovelRepositoryPort tipoImovelRepository;

    public ListTiposImovelUseCase(TipoImovelRepositoryPort tipoImovelRepository) {
        this.tipoImovelRepository = tipoImovelRepository;
    }

    public List<TipoImovelResponse> execute() {
        return tipoImovelRepository.findAll().stream()
                .map(TipoImovelMapper::toResponse)
                .toList();
    }
}
