package br.com.gmob.cliente.api.dto;

import br.com.gmob.infra.domain.enums.TipoInteresseCliente;
import br.com.gmob.infra.validation.ValidationPatterns;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateClienteRequest(
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

        @JsonProperty("tipo_interesse")
        @NotNull(message = "Tipo de interesse é obrigatório")
        TipoInteresseCliente tipoInteresse
) {
}
