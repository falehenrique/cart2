package br.com.exmart.indicadorRTDPJ.ui.util;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class LocalDateToLocalDateTimeConverter implements Converter<LocalDate, LocalDateTime> {
    private ZoneId zoneId;

    public LocalDateToLocalDateTimeConverter(ZoneId zoneId) {
        this.zoneId = (ZoneId) Objects.requireNonNull(zoneId, "Zone identifier cannot be null");
    }

    public Result<LocalDateTime> convertToModel(LocalDate localDate, ValueContext valueContext) {
        return localDate == null ? Result.ok(null) : Result.ok(localDate.atStartOfDay());
    }


    public LocalDate convertToPresentation(LocalDateTime date, ValueContext context) {
        return date == null ? null : date.toLocalDate();
    }
}