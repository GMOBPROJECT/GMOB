package br.com.gmob.infra.infrastructure.persistence.converter;

import br.com.gmob.infra.domain.enums.TipoInteresseCliente;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TipoInteresseClienteConverter implements AttributeConverter<TipoInteresseCliente, String> {

    @Override
    public String convertToDatabaseColumn(TipoInteresseCliente attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public TipoInteresseCliente convertToEntityAttribute(String dbData) {
        return TipoInteresseCliente.fromValue(dbData);
    }
}
