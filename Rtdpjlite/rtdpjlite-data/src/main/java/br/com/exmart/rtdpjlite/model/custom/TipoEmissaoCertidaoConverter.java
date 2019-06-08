package br.com.exmart.rtdpjlite.model.custom;

import br.com.exmart.rtdpjlite.model.TipoEmissaoCertidao;

import javax.persistence.AttributeConverter;

public class TipoEmissaoCertidaoConverter implements AttributeConverter<TipoEmissaoCertidao, Integer> {
    @Override
    public Integer convertToDatabaseColumn(TipoEmissaoCertidao tipoEmissaoCertidao) {
        if ( tipoEmissaoCertidao == null ) {
            return null;
        }

        return tipoEmissaoCertidao.getId();
    }

    @Override
    public TipoEmissaoCertidao convertToEntityAttribute(Integer integer) {
        if ( integer == null ) {
            return null;
        }
        return TipoEmissaoCertidao.fromCode(integer);
    }
}
