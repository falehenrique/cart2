package br.com.exmart.indicadorRTDPJ.ui.custom;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class MyStringToDateTimeConverter  implements Converter<String, LocalDateTime> {
    protected DateTimeFormatter getFormat(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
//        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format;
    }

    @Override
    public Result<LocalDateTime> convertToModel(String value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        }

        // Remove leading and trailing white space
        value = value+" 12:00";


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", context.getLocale().get());
        try {
            LocalDateTime newDate = LocalDateTime.parse(value, dtf);
            return Result.ok(newDate);
        } catch (DateTimeParseException e) {
            return Result.error("Erro ao converter a data: '" + value);
        }

    }

    @Override
    public String convertToPresentation(LocalDateTime value, ValueContext context) {
        if (value == null) {
            return "";
        }
        return getFormat(context.getLocale().orElse(null)).format(value);
    }
}