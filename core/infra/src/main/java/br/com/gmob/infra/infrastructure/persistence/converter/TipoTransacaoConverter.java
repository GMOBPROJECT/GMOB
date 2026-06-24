package br.com.gmob.infra.infrastructure.persistence.converter;

import br.com.gmob.infra.domain.enums.TipoTransacao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TipoTransacaoConverter implements AttributeConverter<TipoTransacao, String> {

    @Override
    public String convertToDatabaseColumn(TipoTransacao attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public TipoTransacao convertToEntityAttribute(String dbData) {
        return TipoTransacao.fromValue(dbData);
    }
}
