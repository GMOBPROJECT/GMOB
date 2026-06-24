package br.com.gmob.cliente.infrastructure.persistence;

import br.com.gmob.imovel.infrastructure.persistence.ImovelJpaEntity;
import br.com.gmob.infra.domain.enums.TipoTransacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import br.com.gmob.infra.infrastructure.persistence.PostgresIntegerId;
import br.com.gmob.infra.infrastructure.persistence.converter.TipoTransacaoConverter;
import jakarta.persistence.Convert;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;

@Entity
@Table(name = "transacoes_imoveis")
public class TransacaoImovelJpaEntity {

    @PostgresIntegerId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transacao_id")
    private Long transacaoId;

    @PostgresIntegerId
    @Column(name = "imovel_id", nullable = false)
    private Long imovelId;

    @PostgresIntegerId
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @PostgresIntegerId
    @Column(name = "corretor_id", nullable = false)
    private Long corretorId;

    @Convert(converter = TipoTransacaoConverter.class)
    @Column(name = "tipo_transacao", nullable = false, columnDefinition = "\"TipoTransacao\"")
    @ColumnTransformer(write = "?::\"TipoTransacao\"")
    private TipoTransacao tipoTransacao;

    @Column(name = "data_transacao", nullable = false)
    private Instant dataTransacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imovel_id", insertable = false, updatable = false)
    private ImovelJpaEntity imovel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", insertable = false, updatable = false)
    private ClienteJpaEntity cliente;

    @PrePersist
    void prePersist() {
        if (dataTransacao == null) {
            dataTransacao = Instant.now();
        }
    }

    public Long getTransacaoId() {
        return transacaoId;
    }

    public void setTransacaoId(Long transacaoId) {
        this.transacaoId = transacaoId;
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

    public Long getCorretorId() {
        return corretorId;
    }

    public void setCorretorId(Long corretorId) {
        this.corretorId = corretorId;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(TipoTransacao tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public Instant getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(Instant dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public ImovelJpaEntity getImovel() {
        return imovel;
    }

    public void setImovel(ImovelJpaEntity imovel) {
        this.imovel = imovel;
    }

    public ClienteJpaEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteJpaEntity cliente) {
        this.cliente = cliente;
    }
}
