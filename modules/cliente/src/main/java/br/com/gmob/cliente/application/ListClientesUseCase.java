package br.com.gmob.cliente.application;

import br.com.gmob.cliente.api.dto.ClienteListResponse;
import br.com.gmob.cliente.api.mapper.ClienteMapper;
import br.com.gmob.cliente.domain.port.ClienteRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.domain.enums.TipoInteresseCliente;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class ListClientesUseCase {

    private final ClienteRepositoryPort clienteRepository;

    public ListClientesUseCase(ClienteRepositoryPort clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteListResponse execute(
            AuthenticatedUser currentUser,
            String tipoInteresse,
            int page,
            int limit
    ) {
        Long corretorId = currentUser.perfil() == Perfil.CORRETOR ? currentUser.corretorId() : null;
        TipoInteresseCliente tipo = parseTipoInteresse(tipoInteresse);

        var clientes = clienteRepository.findAll(corretorId, tipo, page, limit);
        long total = clienteRepository.count(corretorId, tipo);

        return ClienteMapper.toListResponse(clientes, page, limit, total);
    }

    private TipoInteresseCliente parseTipoInteresse(String tipoInteresse) {
        if (tipoInteresse == null || tipoInteresse.isBlank()) {
            return null;
        }
        return TipoInteresseCliente.fromValue(tipoInteresse);
    }
}
