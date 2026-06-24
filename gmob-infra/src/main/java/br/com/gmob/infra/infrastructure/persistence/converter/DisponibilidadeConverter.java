package br.com.gmob.infra.infrastructure.persistence.converter;

import br.com.gmob.infra.domain.enums.Disponibilidade;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class DisponibilidadeConverter implements AttributeConverter<Disponibilidade, String> {

    @Override
    public String convertToDatabaseColumn(Disponibilidade attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public Disponibilidade convertToEntityAttribute(String dbData) {
        return Disponibilidade.fromValue(dbData);
    }
}
