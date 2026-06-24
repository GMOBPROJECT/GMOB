package br.com.gmob.cliente.application;

import br.com.gmob.cliente.api.dto.TransacaoFullResponse;
import br.com.gmob.cliente.api.mapper.TransacaoMapper;
import br.com.gmob.cliente.infrastructure.persistence.SpringDataTransacaoRepository;
import br.com.gmob.cliente.infrastructure.persistence.TransacaoImovelJpaEntity;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class FindTransacaoUseCase {

    private final SpringDataTransacaoRepository transacaoRepository;

    public FindTransacaoUseCase(SpringDataTransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    public TransacaoFullResponse execute(Long id, AuthenticatedUser currentUser) {
        TransacaoImovelJpaEntity transacao = transacaoRepository.findByTransacaoId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transação com ID " + id + " não encontrada."
                ));

        boolean isOwner = transacao.getCorretorId().equals(currentUser.corretorId());
        boolean isAdmin = currentUser.perfil() == Perfil.ADMINISTRADOR;

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException(
                    "Você não tem permissão para visualizar esta transação."
            );
        }

        return TransacaoMapper.toFullResponse(transacao);
    }
}
