package br.com.gmob.imovel.infrastructure.persistence;

import br.com.gmob.infra.domain.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataAgendamentoVisitaRepository extends JpaRepository<AgendamentoVisitaJpaEntity, Long> {

    @Modifying
    @Query("DELETE FROM AgendamentoVisitaJpaEntity a WHERE a.imovelId = :imovelId AND a.corretorId = :corretorId")
    void deleteByImovelIdAndCorretorId(@Param("imovelId") Long imovelId, @Param("corretorId") Long corretorId);

    @Modifying
    @Query("DELETE FROM AgendamentoVisitaJpaEntity a WHERE a.imovelId = :imovelId")
    void deleteByImovelId(@Param("imovelId") Long imovelId);

    @Query(value = """
            SELECT a.agendamento_id, a.imovel_id, a.corretor_id, a.cliente_id,
                   a.data_visita, a.hora_inicio, a.hora_termino, a.observacoes,
                   a.status_agendamento, a.data_agendamento,
                   c.nome, c.telefone, c.email
            FROM agendamentos_visitas a
            INNER JOIN clientes c ON c.cliente_id = a.cliente_id
            WHERE a.imovel_id = :imovelId AND a.status_agendamento = 'agendado'
            """, nativeQuery = true)
    List<Object[]> findAgendadosWithClienteByImovelId(@Param("imovelId") Long imovelId);
}
