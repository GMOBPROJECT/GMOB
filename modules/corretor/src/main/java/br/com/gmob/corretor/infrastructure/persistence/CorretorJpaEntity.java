package br.com.gmob.corretor.infrastructure.persistence;

import br.com.gmob.infra.domain.enums.Perfil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import br.com.gmob.infra.infrastructure.persistence.PostgresIntegerId;
import br.com.gmob.infra.infrastructure.persistence.converter.PerfilConverter;
import jakarta.persistence.Convert;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;

@Entity
@Table(name = "corretores")
public class CorretorJpaEntity {

    @PostgresIntegerId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "corretor_id")
    private Long corretorId;

    @Column(name = "nome_completo", nullable = false, length = 255)
    private String nomeCompleto;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Convert(converter = PerfilConverter.class)
    @Column(name = "perfil", nullable = false, columnDefinition = "\"Perfil\"")
    @ColumnTransformer(write = "?::\"Perfil\"")
    private Perfil perfil;

    @Column(name = "data_cadastro", nullable = false)
    private Instant dataCadastro;

    @PrePersist
    void prePersist() {
        if (dataCadastro == null) {
            dataCadastro = Instant.now();
        }
        if (perfil == null) {
            perfil = Perfil.CORRETOR;
        }
    }

    public Long getCorretorId() {
        return corretorId;
    }

    public void setCorretorId(Long corretorId) {
        this.corretorId = corretorId;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Instant getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Instant dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
