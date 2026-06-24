package br.com.gmob.imovel.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import br.com.gmob.infra.infrastructure.persistence.PostgresIntegerId;
import jakarta.persistence.Table;

@Entity
@Table(name = "imagens_imoveis")
public class ImagemImovelJpaEntity {

    @PostgresIntegerId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imagem_id")
    private Long imagemId;

    @Column(name = "url", nullable = false)
    private String url;

    @PostgresIntegerId
    @Column(name = "imovel_id", nullable = false, insertable = false, updatable = false)
    private Long imovelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imovel_id")
    private ImovelJpaEntity imovel;

    public Long getImagemId() {
        return imagemId;
    }

    public void setImagemId(Long imagemId) {
        this.imagemId = imagemId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getImovelId() {
        return imovelId;
    }

    public void setImovelId(Long imovelId) {
        this.imovelId = imovelId;
    }

    public ImovelJpaEntity getImovel() {
        return imovel;
    }

    public void setImovel(ImovelJpaEntity imovel) {
        this.imovel = imovel;
        if (imovel != null) {
            this.imovelId = imovel.getImovelId();
        }
    }
}
