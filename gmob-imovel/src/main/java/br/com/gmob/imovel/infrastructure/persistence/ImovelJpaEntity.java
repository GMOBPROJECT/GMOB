package br.com.gmob.imovel.infrastructure.persistence;

import br.com.gmob.corretor.infrastructure.persistence.CorretorJpaEntity;
import br.com.gmob.infra.domain.enums.Disponibilidade;
import br.com.gmob.infra.domain.enums.StatusImovel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import br.com.gmob.infra.infrastructure.persistence.PostgresIntegerId;
import br.com.gmob.infra.infrastructure.persistence.converter.DisponibilidadeConverter;
import br.com.gmob.infra.infrastructure.persistence.converter.StatusImovelConverter;
import jakarta.persistence.Convert;
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "imoveis")
public class ImovelJpaEntity {

    @PostgresIntegerId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imovel_id")
    private Long imovelId;

    @PostgresIntegerId
    @Column(name = "corretor_id", nullable = false)
    private Long corretorId;

    @PostgresIntegerId
    @Column(name = "tipo_imovel_id", nullable = false, insertable = false, updatable = false)
    private Long tipoImovelId;

    @Convert(converter = StatusImovelConverter.class)
    @Column(name = "status", nullable = false, columnDefinition = "\"StatusImovel\"")
    @ColumnTransformer(write = "?::\"StatusImovel\"")
    private StatusImovel status;

    @Column(name = "valor_aluguel", precision = 15, scale = 2)
    private BigDecimal valorAluguel;

    @Convert(converter = DisponibilidadeConverter.class)
    @Column(name = "disponibilidade", nullable = false, columnDefinition = "\"Disponibilidade\"")
    @ColumnTransformer(write = "?::\"Disponibilidade\"")
    private Disponibilidade disponibilidade;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @Column(name = "rua", nullable = false, length = 255)
    private String rua;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @Column(name = "complemento", length = 100)
    private String complemento;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "area", nullable = false, precision = 10, scale = 2)
    private BigDecimal area;

    @Column(name = "numero_comodos", nullable = false)
    private Integer numeroComodos;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_cadastro", nullable = false)
    private Instant dataCadastro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corretor_id", insertable = false, updatable = false)
    private CorretorJpaEntity corretor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_imovel_id")
    private TipoImovelJpaEntity tipoImovel;

    @OneToMany(mappedBy = "imovel", fetch = FetchType.LAZY)
    private List<ImagemImovelJpaEntity> imagens = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (dataCadastro == null) {
            dataCadastro = Instant.now();
        }
        if (disponibilidade == null) {
            disponibilidade = Disponibilidade.AMBOS;
        }
    }

    public Long getImovelId() {
        return imovelId;
    }

    public void setImovelId(Long imovelId) {
        this.imovelId = imovelId;
    }

    public Long getCorretorId() {
        return corretorId;
    }

    public void setCorretorId(Long corretorId) {
        this.corretorId = corretorId;
    }

    public Long getTipoImovelId() {
        return tipoImovelId;
    }

    public void setTipoImovelId(Long tipoImovelId) {
        this.tipoImovelId = tipoImovelId;
    }

    public StatusImovel getStatus() {
        return status;
    }

    public void setStatus(StatusImovel status) {
        this.status = status;
    }

    public BigDecimal getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(BigDecimal valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public Disponibilidade getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Disponibilidade disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Integer getNumeroComodos() {
        return numeroComodos;
    }

    public void setNumeroComodos(Integer numeroComodos) {
        this.numeroComodos = numeroComodos;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Instant getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Instant dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public CorretorJpaEntity getCorretor() {
        return corretor;
    }

    public void setCorretor(CorretorJpaEntity corretor) {
        this.corretor = corretor;
    }

    public TipoImovelJpaEntity getTipoImovel() {
        return tipoImovel;
    }

    public void setTipoImovel(TipoImovelJpaEntity tipoImovel) {
        this.tipoImovel = tipoImovel;
        if (tipoImovel != null) {
            this.tipoImovelId = tipoImovel.getTipoImovelId();
        }
    }

    public List<ImagemImovelJpaEntity> getImagens() {
        return imagens;
    }

    public void setImagens(List<ImagemImovelJpaEntity> imagens) {
        this.imagens = imagens;
    }
}
