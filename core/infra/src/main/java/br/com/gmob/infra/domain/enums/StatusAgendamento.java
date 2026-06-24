package br.com.gmob.infra.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusAgendamento {
    AGENDADO("agendado"),
    CONFIRMADO("confirmado"),
    CANCELADO("cancelado"),
    REALIZADO("realizado");

    private final String value;

    StatusAgendamento(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static StatusAgendamento fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (StatusAgendamento status : values()) {
            if (status.value.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status de agendamento inválido: " + value);
    }
}
