package br.com.gmob.corretor.api.dto;

import br.com.gmob.infra.domain.enums.Perfil;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CorretorResponse(
        @JsonProperty("corretor_id")
        Long corretorId,

        @JsonProperty("nome_completo")
        String nomeCompleto,

        String email,

        String telefone,

        String cpf,

        Perfil perfil,

        @JsonProperty("data_cadastro")
        Instant dataCadastro
) {
}
