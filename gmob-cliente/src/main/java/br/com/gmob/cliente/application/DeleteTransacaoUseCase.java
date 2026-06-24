package br.com.gmob.cliente.application;

import br.com.gmob.cliente.api.dto.MessageResponse;
import br.com.gmob.cliente.domain.model.TransacaoImovel;
import br.com.gmob.cliente.domain.port.TransacaoRepositoryPort;
import br.com.gmob.imovel.application.port.ImovelQueryPort;
import br.com.gmob.infra.domain.enums.StatusImovel;
import br.com.gmob.infra.exception.BusinessException;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteTransacaoUseCase {

    private final TransacaoRepositoryPort transacaoRepository;
    private final ImovelQueryPort imovelQueryPort;

    public DeleteTransacaoUseCase(
            TransacaoRepositoryPort transacaoRepository,
            ImovelQueryPort imovelQueryPort
    ) {
        this.transacaoRepository = transacaoRepository;
        this.imovelQueryPort = imovelQueryPort;
    }

    @Transactional
    public MessageResponse execute(Long id, AuthenticatedUser currentUser) {
        TransacaoImovel transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Transação não encontrada"));

        if (!transacao.corretorId().equals(currentUser.corretorId())) {
            throw new ForbiddenException(
                    "Você não tem permissão para remover esta transação"
            );
        }

        transacaoRepository.delete(id);
        imovelQueryPort.updateStatus(transacao.imovelId(), StatusImovel.DISPONIVEL, currentUser);

        return new MessageResponse("Transação removida com sucesso");
    }
}
