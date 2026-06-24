package br.com.gmob.infra.exception;

public record ErrorResponse(
        int statusCode,
        Object message,
        String error
) {
}
