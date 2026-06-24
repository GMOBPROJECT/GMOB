package br.com.gmob.infra.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusImovel {
    DISPONIVEL("disponivel"),
    VENDIDO("vendido"),
    ALUGADO("alugado");

    private final String value;

    StatusImovel(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static StatusImovel fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (StatusImovel status : values()) {
            if (status.value.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status de imóvel inválido: " + value);
    }
}
