package br.com.gmob.cliente.api.dto;

import br.com.gmob.infra.domain.enums.TipoInteresseCliente;
import br.com.gmob.infra.validation.ValidationPatterns;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UpdateClienteRequest(
        @JsonProperty("nome_completo")
        String nomeCompleto,

        @Email(message = "Email deve ter um formato válido")
        String email,

        @Pattern(regexp = ValidationPatterns.TELEFONE_PATTERN,
                message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
        String telefone,

        @Pattern(regexp = ValidationPatterns.CPF_PATTERN,
                message = "CPF deve estar no formato XXX.XXX.XXX-XX")
        String cpf,

        @JsonProperty("tipo_interesse")
        TipoInteresseCliente tipoInteresse
) {
}
