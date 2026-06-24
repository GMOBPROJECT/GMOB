package br.com.gmob.imovel.application.port;

public interface AgendamentoCommandPort {

    void deleteByImovelId(Long imovelId, Long corretorId, boolean isAdmin);
}
