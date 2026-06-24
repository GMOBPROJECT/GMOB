package br.com.gmob.infra.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoTransacao {
    VENDA("venda"),
    ALUGUEL("aluguel");

    private final String value;

    TipoTransacao(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TipoTransacao fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (TipoTransacao tipo : values()) {
            if (tipo.value.equalsIgnoreCase(value) || tipo.name().equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de transação inválido: " + value);
    }
}
