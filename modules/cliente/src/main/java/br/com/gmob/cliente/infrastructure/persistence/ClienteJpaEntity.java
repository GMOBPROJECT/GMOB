package br.com.gmob.cliente.infrastructure.persistence;

import br.com.gmob.infra.domain.enums.TipoInteresseCliente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import br.com.gmob.infra.infrastructure.persistence.PostgresIntegerId;
import br.com.gmob.infra.infrastructure.persistence.converter.TipoInteresseClienteConverter;
import jakarta.persistence.Convert;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;

@Entity
@Table(name = "clientes")
public class ClienteJpaEntity {

    @PostgresIntegerId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long clienteId;

    @PostgresIntegerId
    @Column(name = "corretor_id", nullable = false)
    private Long corretorId;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Convert(converter = TipoInteresseClienteConverter.class)
    @Column(name = "tipo_interesse", nullable = false, columnDefinition = "\"TipoInteresseCliente\"")
    @ColumnTransformer(write = "?::\"TipoInteresseCliente\"")
    private TipoInteresseCliente tipoInteresse;

    @Column(name = "arquivado", nullable = false)
    private boolean arquivado;

    @Column(name = "data_cadastro", nullable = false)
    private Instant dataCadastro;

    @PrePersist
    void prePersist() {
        if (dataCadastro == null) {
            dataCadastro = Instant.now();
        }
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public TipoInteresseCliente getTipoInteresse() {
        return tipoInteresse;
    }

    public void setTipoInteresse(TipoInteresseCliente tipoInteresse) {
        this.tipoInteresse = tipoInteresse;
    }

    public boolean isArquivado() {
        return arquivado;
    }

    public void setArquivado(boolean arquivado) {
        this.arquivado = arquivado;
    }

    public Instant getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Instant dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
