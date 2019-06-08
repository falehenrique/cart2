package br.com.exmart.indicadorRTDPJ.ui.custom.event;

import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import org.joda.money.Money;

import java.text.ParseException;

public class MyStringToMoneyConverter implements Converter<String, Money> {
    @Override
    public Result<Money> convertToModel(String value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        }
//
//        // Remove leading and trailing white space
//        value = value.trim();
//
//        ParsePosition parsePosition = new ParsePosition(0);
//        Date parsedValue = getFormat(context.getLocale().orElse(null))
//                .parse(value, parsePosition);
//        if (parsePosition.getIndex() != value.length()) {
//            return Result.error("Could not convert '" + value);
//        }

        Money retorno = null;
        try {
            retorno = Utils.convertToMoney(value);
        } catch (ParseException e) {
            retorno = Money.zero(Utils.BRL);
        }

        return Result.ok(retorno);
    }

    @Override
    public String convertToPresentation(Money value, ValueContext context) {
        return Utils.convertFromMoney(value);
    }
}
