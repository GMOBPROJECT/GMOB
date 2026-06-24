package br.com.gmob.infra.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoInteresseCliente {
    COMPRA("compra"),
    ALUGUEL("aluguel");

    private final String value;

    TipoInteresseCliente(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TipoInteresseCliente fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (TipoInteresseCliente tipo : values()) {
            if (tipo.value.equalsIgnoreCase(value) || tipo.name().equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de interesse inválido: " + value);
    }
}
