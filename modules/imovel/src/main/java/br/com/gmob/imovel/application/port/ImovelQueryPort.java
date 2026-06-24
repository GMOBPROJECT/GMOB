package br.com.gmob.imovel.application.port;

import br.com.gmob.infra.domain.enums.StatusImovel;
import br.com.gmob.infra.security.AuthenticatedUser;
import br.com.gmob.imovel.domain.model.ImovelTransactionView;

public interface ImovelQueryPort {

    ImovelTransactionView findByIdForTransaction(Long id, AuthenticatedUser user);

    void updateStatus(Long id, StatusImovel status, AuthenticatedUser user);
}
