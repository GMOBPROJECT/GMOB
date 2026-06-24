package br.com.gmob.corretor.api.dto;

import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.validation.ValidationPatterns;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateCorretorRequest(
        @JsonProperty("nome_completo")
        @NotBlank(message = "Nome completo é obrigatório")
        String nomeCompleto,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ter um formato válido")
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = ValidationPatterns.TELEFONE_PATTERN,
                message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
        String telefone,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = ValidationPatterns.CPF_PATTERN,
                message = "CPF deve estar no formato XXX.XXX.XXX-XX")
        String cpf,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
        String senha,

        Perfil perfil
) {
}
