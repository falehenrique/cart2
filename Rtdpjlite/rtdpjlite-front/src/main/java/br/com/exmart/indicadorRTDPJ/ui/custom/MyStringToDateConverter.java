package br.com.exmart.indicadorRTDPJ.ui.custom;

import com.google.common.base.Strings;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class MyStringToDateConverter implements Converter<String, LocalDate> {
    protected DateTimeFormatter getFormat(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format;
    }

    @Override
    public Result<LocalDate> convertToModel(String value, ValueContext context) {
        if (Strings.isNullOrEmpty(value)) {
            return Result.ok(null);
        }

        // Remove leading and trailing white space
        value = value.trim();


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy", context.getLocale().get());
        try {
            LocalDate newDate = LocalDate.parse(value, dtf);
            return Result.ok(newDate);
        } catch (DateTimeParseException e) {
            return Result.error("Erro ao converter a data: '" + value);
        }

    }

    @Override
    public String convertToPresentation(LocalDate value, ValueContext context) {
        if (value == null) {
            return "";
        }
        return getFormat(context.getLocale().orElse(null)).format(value);
    }
}
