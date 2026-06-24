package br.com.gmob.imovel.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import br.com.gmob.infra.infrastructure.persistence.PostgresIntegerId;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipos_imoveis")
public class TipoImovelJpaEntity {

    @PostgresIntegerId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_imovel_id")
    private Long tipoImovelId;

    @Column(name = "nome_tipo", nullable = false, unique = true, length = 50)
    private String nomeTipo;

    public Long getTipoImovelId() {
        return tipoImovelId;
    }

    public void setTipoImovelId(Long tipoImovelId) {
        this.tipoImovelId = tipoImovelId;
    }

    public String getNomeTipo() {
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }
}
