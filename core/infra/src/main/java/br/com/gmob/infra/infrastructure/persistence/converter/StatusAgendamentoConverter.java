package br.com.gmob.infra.infrastructure.persistence.converter;

import br.com.gmob.infra.domain.enums.StatusAgendamento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class StatusAgendamentoConverter implements AttributeConverter<StatusAgendamento, String> {

    @Override
    public String convertToDatabaseColumn(StatusAgendamento attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public StatusAgendamento convertToEntityAttribute(String dbData) {
        return StatusAgendamento.fromValue(dbData);
    }
}
