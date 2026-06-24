package br.com.gmob.imovel.infrastructure.persistence;

import br.com.gmob.imovel.application.port.AgendamentoCommandPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AgendamentoCommandAdapter implements AgendamentoCommandPort {

    private final SpringDataAgendamentoVisitaRepository repository;

    public AgendamentoCommandAdapter(SpringDataAgendamentoVisitaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void deleteByImovelId(Long imovelId, Long corretorId, boolean isAdmin) {
        if (isAdmin) {
            repository.deleteByImovelId(imovelId);
        } else {
            repository.deleteByImovelIdAndCorretorId(imovelId, corretorId);
        }
    }
}
