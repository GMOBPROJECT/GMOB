package br.com.gmob.cliente.api.mapper;

import br.com.gmob.cliente.api.dto.ClienteListResponse;
import br.com.gmob.cliente.api.dto.ClienteResponse;
import br.com.gmob.cliente.domain.model.Cliente;
import br.com.gmob.infra.dto.PaginationResponse;

import java.util.List;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.id(),
                cliente.corretorId(),
                cliente.nome(),
                cliente.cpf(),
                cliente.email(),
                cliente.telefone(),
                cliente.tipoInteresse(),
                cliente.arquivado(),
                cliente.dataCadastro()
        );
    }

    public static List<ClienteResponse> toResponseList(List<Cliente> clientes) {
        return clientes.stream()
                .map(ClienteMapper::toResponse)
                .toList();
    }

    public static ClienteListResponse toListResponse(List<Cliente> clientes, int page, int limit, long total) {
        int totalPages = limit > 0 ? (int) Math.ceil((double) total / limit) : 0;
        return new ClienteListResponse(
                toResponseList(clientes),
                new PaginationResponse(page, limit, total, totalPages)
        );
    }
}
