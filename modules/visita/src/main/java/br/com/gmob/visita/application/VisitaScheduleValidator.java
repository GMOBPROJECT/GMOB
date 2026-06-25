package br.com.gmob.visita.application;

import br.com.gmob.infra.exception.ConflictException;
import br.com.gmob.infra.exception.ForbiddenException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class VisitaScheduleValidator {

    private VisitaScheduleValidator() {
    }

    public static void validateFutureSchedule(LocalDate dataVisita, LocalTime horaInicio, LocalTime horaTermino) {
        LocalDateTime inicio = LocalDateTime.of(dataVisita, horaInicio);
        if (!inicio.isAfter(LocalDateTime.now())) {
            throw new ForbiddenException("A data da visita deve ser futura");
        }
        if (!horaTermino.isAfter(horaInicio)) {
            throw new ForbiddenException("O horário de término deve ser maior que o horário de início");
        }
    }

    public static LocalTime parseTime(String value) {
        if (value == null || value.isBlank()) {
            throw new ConflictException("Horário inválido");
        }
        return value.length() == 5 ? LocalTime.parse(value + ":00") : LocalTime.parse(value);
    }

    public static LocalDate parseDate(String value) {
        return LocalDate.parse(value);
    }
}
