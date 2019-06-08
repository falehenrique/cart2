package br.com.exmart.indicadorRTDPJ.ui.util;

import br.com.exmart.rtdpjlite.model.Natureza;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public class MyAbstractEntityToLongConverter implements Converter<Natureza, Long> {
    @Override
    public Result<Long> convertToModel(Natureza abstractEntity, ValueContext valueContext) {
        return null;
    }

    @Override
    public Natureza convertToPresentation(Long aLong, ValueContext valueContext) {
        return null;
    }
}
