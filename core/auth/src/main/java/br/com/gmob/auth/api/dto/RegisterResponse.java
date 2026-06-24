package br.com.gmob.auth.api.dto;

import br.com.gmob.corretor.api.dto.CorretorResponse;

public record RegisterResponse(
        String message,
        CorretorResponse corretor
) {
}
