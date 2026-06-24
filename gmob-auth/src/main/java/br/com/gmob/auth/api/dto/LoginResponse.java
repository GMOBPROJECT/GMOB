package br.com.gmob.auth.api.dto;

import br.com.gmob.corretor.api.dto.CorretorResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
        @JsonProperty("access_token")
        String accessToken,

        CorretorResponse user
) {
}
