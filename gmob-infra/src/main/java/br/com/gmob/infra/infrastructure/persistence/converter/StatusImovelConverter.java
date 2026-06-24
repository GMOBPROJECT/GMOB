package br.com.gmob.infra.infrastructure.persistence.converter;

import br.com.gmob.infra.domain.enums.StatusImovel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class StatusImovelConverter implements AttributeConverter<StatusImovel, String> {

    @Override
    public String convertToDatabaseColumn(StatusImovel attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public StatusImovel convertToEntityAttribute(String dbData) {
        return StatusImovel.fromValue(dbData);
    }
}
