package br.com.exmart.rtdpjlite.model.custom;

import br.com.exmart.rtdpjlite.model.TipoProtocolo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TipoProtocoloConverter implements AttributeConverter<TipoProtocolo, Integer>{
    public Integer convertToDatabaseColumn(TipoProtocolo value) {
        if ( value == null ) {
            return null;
        }

        return value.getId();
    }

    public TipoProtocolo convertToEntityAttribute(Integer value) {
        if ( value == null ) {
            return null;
        }

        return TipoProtocolo.fromCode(value);
    }
}
