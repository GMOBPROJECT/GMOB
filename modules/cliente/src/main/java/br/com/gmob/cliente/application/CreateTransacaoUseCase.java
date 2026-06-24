package br.com.gmob.cliente.application;

import br.com.gmob.cliente.api.dto.CreateTransacaoRequest;
import br.com.gmob.cliente.api.dto.CreateTransacaoResultResponse;
import br.com.gmob.cliente.api.mapper.TransacaoMapper;
import br.com.gmob.cliente.application.port.ClienteLookupPort;
import br.com.gmob.cliente.domain.model.Cliente;
import br.com.gmob.cliente.domain.model.TransacaoImovel;
import br.com.gmob.cliente.domain.port.TransacaoRepositoryPort;
import br.com.gmob.imovel.application.port.AgendamentoVisitaQueryPort;
import br.com.gmob.imovel.application.port.ImovelQueryPort;
import br.com.gmob.infra.domain.enums.StatusImovel;
import br.com.gmob.infra.domain.enums.TipoTransacao;
import br.com.gmob.infra.exception.BusinessException;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
public class CreateTransacaoUseCase {

    private final ClienteLookupPort clienteLookupPort;
    private final ImovelQueryPort imovelQueryPort;
    private final TransacaoRepositoryPort transacaoRepository;
    private final AgendamentoVisitaQueryPort agendamentoVisitaQueryPort;

    public CreateTransacaoUseCase(
            ClienteLookupPort clienteLookupPort,
            ImovelQueryPort imovelQueryPort,
            TransacaoRepositoryPort transacaoRepository,
            AgendamentoVisitaQueryPort agendamentoVisitaQueryPort
    ) {
        this.clienteLookupPort = clienteLookupPort;
        this.imovelQueryPort = imovelQueryPort;
        this.transacaoRepository = transacaoRepository;
        this.agendamentoVisitaQueryPort = agendamentoVisitaQueryPort;
    }

    @Transactional
    public CreateTransacaoResultResponse execute(CreateTransacaoRequest request, AuthenticatedUser currentUser) {
        Cliente cliente = clienteLookupPort.findByCpf(request.cpf());

        if (!currentUser.corretorId().equals(cliente.corretorId())) {
            throw new ForbiddenException(
                    "Você não tem permissão para realizar transações para este cliente."
            );
        }

        var imovel = imovelQueryPort.findByIdForTransaction(request.imovelId(), currentUser);

        if (imovel.status() != StatusImovel.DISPONIVEL) {
            throw new BusinessException("Imóvel não está disponível para transação.");
        }

        StatusImovel novoStatus = request.tipoTransacao() == TipoTransacao.VENDA
                ? StatusImovel.VENDIDO
                : StatusImovel.ALUGADO;

        imovelQueryPort.updateStatus(request.imovelId(), novoStatus, currentUser);

        TransacaoImovel transacao = transacaoRepository.save(new TransacaoImovel(
                null,
                request.imovelId(),
                cliente.id(),
                currentUser.corretorId(),
                request.tipoTransacao(),
                Instant.now()
        ));

        List<AgendamentoVisitaQueryPort.AgendamentoPendenteView> agendamentos =
                request.tipoTransacao() == TipoTransacao.VENDA
                        ? agendamentoVisitaQueryPort.findAgendadosByImovelId(transacao.imovelId())
                        : Collections.emptyList();

        return TransacaoMapper.toCreateResult(transacao, agendamentos);
    }
}
