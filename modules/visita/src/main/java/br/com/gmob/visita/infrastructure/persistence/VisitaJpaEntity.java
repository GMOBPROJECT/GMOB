package br.com.gmob.visita.infrastructure.persistence;

import br.com.gmob.infra.domain.enums.StatusAgendamento;
import br.com.gmob.infra.infrastructure.persistence.PostgresIntegerId;
import br.com.gmob.infra.infrastructure.persistence.converter.StatusAgendamentoConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "agendamentos_visitas")
public class VisitaJpaEntity {

    @PostgresIntegerId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agendamento_id")
    private Long agendamentoId;

    @PostgresIntegerId
    @Column(name = "corretor_id", nullable = false)
    private Long corretorId;

    @PostgresIntegerId
    @Column(name = "imovel_id", nullable = false)
    private Long imovelId;

    @PostgresIntegerId
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "data_visita", nullable = false)
    private LocalDate dataVisita;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_termino", nullable = false)
    private LocalTime horaTermino;

    @Column(name = "observacoes")
    private String observacoes;

    @Convert(converter = StatusAgendamentoConverter.class)
    @Column(name = "status_agendamento", nullable = false, columnDefinition = "\"StatusAgendamento\"")
    @ColumnTransformer(write = "?::\"StatusAgendamento\"")
    private StatusAgendamento statusAgendamento;

    @Column(name = "data_agendamento", nullable = false)
    private Instant dataAgendamento;

    public Long getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(Long agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public Long getCorretorId() {
        return corretorId;
    }

    public void setCorretorId(Long corretorId) {
        this.corretorId = corretorId;
    }

    public Long getImovelId() {
        return imovelId;
    }

    public void setImovelId(Long imovelId) {
        this.imovelId = imovelId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDate getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(LocalDate dataVisita) {
        this.dataVisita = dataVisita;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(LocalTime horaTermino) {
        this.horaTermino = horaTermino;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public StatusAgendamento getStatusAgendamento() {
        return statusAgendamento;
    }

    public void setStatusAgendamento(StatusAgendamento statusAgendamento) {
        this.statusAgendamento = statusAgendamento;
    }

    public Instant getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(Instant dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }
}
