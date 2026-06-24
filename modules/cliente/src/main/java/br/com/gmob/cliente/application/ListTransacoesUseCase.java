package br.com.gmob.cliente.application;

import br.com.gmob.cliente.api.dto.TransacaoListResponse;
import br.com.gmob.cliente.api.mapper.TransacaoMapper;
import br.com.gmob.cliente.domain.port.TransacaoRepositoryPort;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class ListTransacoesUseCase {

    private final TransacaoRepositoryPort transacaoRepository;

    public ListTransacoesUseCase(TransacaoRepositoryPort transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    public TransacaoListResponse execute(AuthenticatedUser currentUser, int page, int limit) {
        var transacoes = transacaoRepository.findAllByCorretorId(currentUser.corretorId(), page, limit);
        long total = transacaoRepository.countByCorretorId(currentUser.corretorId());

        return TransacaoMapper.toListResponse(transacoes, page, limit, total);
    }
}
