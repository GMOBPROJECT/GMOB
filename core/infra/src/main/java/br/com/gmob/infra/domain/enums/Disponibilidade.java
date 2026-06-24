package br.com.gmob.infra.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Disponibilidade {
    VENDA("venda"),
    ALUGUEL("aluguel"),
    AMBOS("ambos");

    private final String value;

    Disponibilidade(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Disponibilidade fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (Disponibilidade disponibilidade : values()) {
            if (disponibilidade.value.equalsIgnoreCase(value) || disponibilidade.name().equalsIgnoreCase(value)) {
                return disponibilidade;
            }
        }
        throw new IllegalArgumentException("Disponibilidade inválida: " + value);
    }
}
