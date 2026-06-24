package br.com.gmob.infra.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Perfil {
    CORRETOR("corretor"),
    ADMINISTRADOR("administrador");

    private final String value;

    Perfil(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Perfil fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (Perfil perfil : values()) {
            if (perfil.value.equalsIgnoreCase(value) || perfil.name().equalsIgnoreCase(value)) {
                return perfil;
            }
        }
        throw new IllegalArgumentException("Perfil inválido: " + value);
    }
}
