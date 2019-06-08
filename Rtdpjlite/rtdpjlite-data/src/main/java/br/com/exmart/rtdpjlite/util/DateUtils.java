package br.com.exmart.rtdpjlite.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private DateUtils(){}

    /**
     * Retorna o valor do horï¿½rio minimo para a data de referencia passada.
     * <BR>
     * <BR> Por exemplo se a data for "30/01/2009 as 17h:33m:12s e 299ms" a data
     * retornada por este metodo serï¿½ "30/01/2009 as 00h:00m:00s e 000ms".
     * @param date de referencia.
     * @return {@link Date} que representa o horï¿½rio minimo para dia informado.
     */
    public static Date lowDateTime(Date date) {
        Calendar aux = Calendar.getInstance();
        aux.setTime(date);
        toOnlyDate(aux); //zera os parametros de hour,min,sec,milisec
        return aux.getTime();
    }

    /**
     * Retorna o valor do horï¿½rio maximo para a data de referencia passada.
     * <BR>
     * <BR> Por exemplo se a data for "30/01/2009 as 17h:33m:12s e 299ms" a data
     * retornada por este metodo serï¿½ "30/01/2009 as 23h:59m:59s e 999ms".
     * @param date de referencia.
     * @return {@link Date} que representa o horï¿½rio maximo para dia informado.
     */
    public static Date highDateTime(Date date) {
        Calendar aux = Calendar.getInstance();
        aux.setTime(date);
        topHora(aux); //zera os parametros de hour,min,sec,milisec
        return aux.getTime();
    }

    public static Date lastDayMonth(Date date){
        Calendar aux = Calendar.getInstance();
        aux.setTime(date);
        aux.set(Calendar.DAY_OF_MONTH, aux.getActualMaximum(Calendar.DAY_OF_MONTH));
        return aux.getTime();
    }

    public static Date firstDayMonth(Date date){
        Calendar aux = Calendar.getInstance();
        aux.setTime(date);
        aux.set(Calendar.DAY_OF_MONTH, aux.getActualMinimum(Calendar.DAY_OF_MONTH));
        return aux.getTime();
    }
    public static void topHora(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);
        date.set(Calendar.MILLISECOND, 250);
    }

    /**
     * Zera todas as referencias de hora, minuto, segundo e milesegundo do
     * {@link Calendar}.
     * @param date a ser modificado.
     */
    public static void toOnlyDate(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
    }
}