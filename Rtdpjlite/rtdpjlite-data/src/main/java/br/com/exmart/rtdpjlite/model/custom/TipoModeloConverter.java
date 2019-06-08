package br.com.exmart.rtdpjlite.model.custom;

import br.com.exmart.rtdpjlite.model.TipoModelo;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TipoModeloConverter implements AttributeConverter<TipoModelo, Integer> {
    public Integer convertToDatabaseColumn(TipoModelo value) {
        if ( value == null ) {
            return null;
        }

        return value.getId();
    }

    public TipoModelo convertToEntityAttribute(Integer value) {
        if ( value == null ) {
            return null;
        }

        return TipoModelo.fromCode(value);
    }
}
