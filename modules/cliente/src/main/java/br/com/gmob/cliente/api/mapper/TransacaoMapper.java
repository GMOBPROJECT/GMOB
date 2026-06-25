package br.com.gmob.cliente.api.mapper;

import br.com.gmob.cliente.api.dto.AgendamentoPendenteResponse;
import br.com.gmob.cliente.api.dto.ClienteAgendamentoResumoResponse;
import br.com.gmob.cliente.api.dto.ClienteResumoResponse;
import br.com.gmob.cliente.api.dto.ClienteResponse;
import br.com.gmob.cliente.api.dto.CreateTransacaoResultResponse;
import br.com.gmob.cliente.api.dto.ImovelFullResponse;
import br.com.gmob.cliente.api.dto.ImovelResumoResponse;
import br.com.gmob.cliente.api.dto.TransacaoDetalheResponse;
import br.com.gmob.cliente.api.dto.TransacaoFullResponse;
import br.com.gmob.cliente.api.dto.TransacaoListResponse;
import br.com.gmob.cliente.api.dto.TransacaoResponse;
import br.com.gmob.cliente.domain.model.ClienteResumo;
import br.com.gmob.cliente.domain.model.ImovelResumo;
import br.com.gmob.cliente.domain.model.TransacaoImovel;
import br.com.gmob.cliente.domain.model.TransacaoImovelDetalhe;
import br.com.gmob.cliente.infrastructure.persistence.ClienteJpaEntity;
import br.com.gmob.cliente.infrastructure.persistence.TransacaoImovelJpaEntity;
import br.com.gmob.imovel.infrastructure.persistence.ImovelJpaEntity;
import br.com.gmob.visita.application.port.VisitaQueryPort;
import br.com.gmob.infra.dto.PaginationResponse;

import java.util.List;

public final class TransacaoMapper {

    private TransacaoMapper() {
    }

    public static TransacaoResponse toResponse(TransacaoImovel transacao) {
        return new TransacaoResponse(
                transacao.id(),
                transacao.imovelId(),
                transacao.clienteId(),
                transacao.corretorId(),
                transacao.tipoTransacao(),
                transacao.dataTransacao()
        );
    }

    public static ImovelResumoResponse toImovelResumoResponse(ImovelResumo imovel) {
        if (imovel == null) {
            return null;
        }
        return new ImovelResumoResponse(
                imovel.id(),
                imovel.rua(),
                imovel.numero(),
                imovel.cidade(),
                imovel.valor(),
                imovel.valorAluguel()
        );
    }

    public static ClienteResumoResponse toClienteResumoResponse(ClienteResumo cliente) {
        if (cliente == null) {
            return null;
        }
        return new ClienteResumoResponse(
                cliente.id(),
                cliente.nome()
        );
    }

    public static TransacaoDetalheResponse toDetalheResponse(TransacaoImovelDetalhe detalhe) {
        return new TransacaoDetalheResponse(
                detalhe.id(),
                detalhe.imovelId(),
                detalhe.clienteId(),
                detalhe.corretorId(),
                detalhe.tipoTransacao(),
                detalhe.dataTransacao(),
                toImovelResumoResponse(detalhe.imovel()),
                toClienteResumoResponse(detalhe.cliente())
        );
    }

    public static List<TransacaoDetalheResponse> toDetalheResponseList(List<TransacaoImovelDetalhe> detalhes) {
        return detalhes.stream()
                .map(TransacaoMapper::toDetalheResponse)
                .toList();
    }

    public static TransacaoListResponse toListResponse(
            List<TransacaoImovelDetalhe> detalhes,
            int page,
            int limit,
            long total
    ) {
        int totalPages = limit > 0 ? (int) Math.ceil((double) total / limit) : 0;
        return new TransacaoListResponse(
                toDetalheResponseList(detalhes),
                new PaginationResponse(page, limit, total, totalPages)
        );
    }

    public static ImovelFullResponse toImovelFullResponse(ImovelJpaEntity imovel) {
        if (imovel == null) {
            return null;
        }
        return new ImovelFullResponse(
                imovel.getImovelId(),
                imovel.getCorretorId(),
                imovel.getTipoImovelId(),
                imovel.getStatus(),
                imovel.getDisponibilidade(),
                imovel.getValorAluguel(),
                imovel.getEstado(),
                imovel.getCidade(),
                imovel.getRua(),
                imovel.getNumero(),
                imovel.getComplemento(),
                imovel.getValor(),
                imovel.getArea(),
                imovel.getNumeroComodos(),
                imovel.getDescricao(),
                imovel.getDataCadastro()
        );
    }

    public static TransacaoFullResponse toFullResponse(TransacaoImovelJpaEntity entity) {
        ClienteResponse clienteResponse = null;
        if (entity.getCliente() != null) {
            ClienteJpaEntity cliente = entity.getCliente();
            clienteResponse = new ClienteResponse(
                    cliente.getClienteId(),
                    cliente.getCorretorId(),
                    cliente.getNome(),
                    cliente.getCpf(),
                    cliente.getEmail(),
                    cliente.getTelefone(),
                    cliente.getTipoInteresse(),
                    cliente.isArquivado(),
                    cliente.getDataCadastro()
            );
        }
        return new TransacaoFullResponse(
                entity.getTransacaoId(),
                entity.getImovelId(),
                entity.getClienteId(),
                entity.getCorretorId(),
                entity.getTipoTransacao(),
                entity.getDataTransacao(),
                toImovelFullResponse(entity.getImovel()),
                clienteResponse
        );
    }

    public static CreateTransacaoResultResponse toCreateResult(
            TransacaoImovel transacao,
            List<VisitaQueryPort.VisitaPendenteView> agendamentos
    ) {
        return new CreateTransacaoResultResponse(
                toResponse(transacao),
                agendamentos.stream()
                        .map(TransacaoMapper::toAgendamentoPendenteResponse)
                        .toList()
        );
    }

    public static AgendamentoPendenteResponse toAgendamentoPendenteResponse(
            VisitaQueryPort.VisitaPendenteView view
    ) {
        ClienteAgendamentoResumoResponse clienteResumo = null;
        if (view.clienteNome() != null) {
            clienteResumo = new ClienteAgendamentoResumoResponse(
                    view.clienteNome(),
                    view.clienteTelefone(),
                    view.clienteEmail()
            );
        }
        return new AgendamentoPendenteResponse(
                view.agendamentoId(),
                view.imovelId(),
                view.corretorId(),
                view.clienteId(),
                view.dataVisita(),
                view.horaInicio(),
                view.horaTermino(),
                view.observacoes(),
                view.statusAgendamento(),
                view.dataAgendamento(),
                clienteResumo
        );
    }
}
