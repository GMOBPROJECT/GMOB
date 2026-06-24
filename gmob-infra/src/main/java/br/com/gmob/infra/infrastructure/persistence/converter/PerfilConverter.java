package br.com.gmob.infra.infrastructure.persistence.converter;

import br.com.gmob.infra.domain.enums.Perfil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class PerfilConverter implements AttributeConverter<Perfil, String> {

    @Override
    public String convertToDatabaseColumn(Perfil attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public Perfil convertToEntityAttribute(String dbData) {
        return Perfil.fromValue(dbData);
    }
}
