package br.com.exmart.indicadorRTDPJ.ui.util;

import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloVO;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import br.com.exmart.util.BeanLocator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.icons.VaadinIcons;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fabioebner on 15/07/17.
 */
public class Utils {
    public static final CurrencyUnit BRL = CurrencyUnit.of("BRL");
    static DecimalFormat decimalFormat = null;
    static DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static Locale ptBr = new Locale("pt", "BR");
    private static String dtFormat= "dd/MM/yyyy";
    private static ObjectMapper jsonConverter;
    static {
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        String pattern = "#,##0.00";
        decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
        jsonConverter = BeanLocator.find(ObjectMapper.class);
    }

    public static String formatarData(LocalDate data){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dtFormat);
        return data.format(formatter);

    }

    public static String formatarDataComHora(LocalDateTime data){
        if(data!=null) {
    	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
         return data.format(formatter);
        }
     
        return "";
    }
    
    public static String formatarDataComHora(Date data){
        if(data!=null) {
    	 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
         return formatter.format(data);
        }
     
        return "";
    }


    public static String formatarData(Date data){
        return dateFormat.format(data);
    }

    public static Date localDateToDate(LocalDate localDate){
        if(localDate == null){
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    public static Date localDateToDateEndDay(LocalDate localDate){
        if(localDate == null){
            return null;
        }
        Calendar diaAtual = Calendar.getInstance();
        diaAtual.setTime(Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        diaAtual.set(Calendar.HOUR_OF_DAY, 23);
        diaAtual.set(Calendar.MINUTE, 59);
        diaAtual.set(Calendar.SECOND, 59);
        return diaAtual.getTime();
    }

    public static Date localDateTimeToDate(LocalDateTime localDate){
        if(localDate == null){
            return null;
        }
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public static Money convertToMoney(String value) throws ParseException {
        if(value == null){
            return Money.zero(BRL);
        }
        return Money.of(BRL, (BigDecimal) Utils.getDecimalFormat().parse(value));
    }
    public static String convertFromMoney(Money value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        if (value!=null) {
            return formatter.format(value.getAmount());
        }
        return "0,00";
    }

    public static String getNomeCorretoFromTipoProtocolo(TipoProtocolo tipo) {
        switch (tipo){
            case CERTIDAO_PJ:
                return "Certidão - PJ ";
            case CERTIDAO_TD:
                return "Certidão - TD ";
            case EXAMECALCULO_PJ:
                return "Exame e Cálculo - PJ ";
            case EXAMECALCULO_TD:
                return "Exame e Cálculo - TD ";
            case PRENOTACAO_PJ:
                return "Registro - PJ ";
            case PRENOTACAO_TD:
                return "Registro - TD ";
            default:
                return "Não identificado";
        }

    }

    public static VaadinIcons getIconCorretoFromTipoProtocolo(TipoProtocolo tipo) {
        switch (tipo){
            case CERTIDAO_PJ:
                return VaadinIcons.FILE_PICTURE;
            case CERTIDAO_TD:
                return VaadinIcons.FILE_PICTURE;
            case EXAMECALCULO_PJ:
                return VaadinIcons.DOLLAR;
            case EXAMECALCULO_TD:
                return VaadinIcons.DOLLAR;
            case PRENOTACAO_PJ:
                return VaadinIcons.LIST_OL;
            case PRENOTACAO_TD:
                return VaadinIcons.LIST_OL;
            default:
                return VaadinIcons.WARNING;
        }
    }

    public static String statusProtocoloJsonToJson(StatusProtocoloJson objeto) throws JsonProcessingException {
        return jsonConverter.writeValueAsString(objeto);
    }
    public static StatusProtocoloJson statusStringToProtocoloJson(String objeto) throws IOException {
        return jsonConverter.readValue(objeto, StatusProtocoloJson.class);
    }
    public static String objetoProtocoloVOToJson(List<ObjetoProtocoloVO> objeto) throws JsonProcessingException {
        return jsonConverter.writeValueAsString(objeto);
    }

    public static void validarDocumento(String value) {

    }

    public static LocalDate dateToLocalDate(Date dataProtocolo) {
        return dataProtocolo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    public static String formatarDataExtenso(LocalDate data) {
        // PEGO AQUI A DATA ATUAL
        Date dataAtual = localDateToDate(data);

        // CRIO AQUI UM FORMATADOR
        // PASSANDO UM ESTILO DE FORMATAÇÃO : DateFormat.FULL
        // E PASSANDO UM LOCAL DA DATA : new Locale("pt", "BR")
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));

        // FORMATO A DATA, O FORMATADOR ME RETORNA
        // A STRING DA DATA FORMATADA
        String dataExtenso = formatador.format(dataAtual);


        // MOSTRA A DATA
//        System.out.println("Data com o dia da semana : " + dataExtenso);


        // AQUI É CASO VOCÊ QUEIRA TIRAR
        // O DIA DA SEMANA QUE APARECE NO
        // PRIMEIRO EXEMPLO
        int index  = dataExtenso.indexOf(",");
        int lenght = dataExtenso.length();


        // MOSTRA A DATA
        return dataExtenso.substring(++index, lenght);
    }

    public static LocalDate stringtoLocalDate(String data) {
        if(data == null)
            return null;
        return LocalDate.parse(data, localDateFormat);
    }

    public static String localDateToString(LocalDate data) {
        if(data == null)
            return "";
        return data.format(localDateFormat);
    }

    public static String formatarData(LocalDateTime dtRegistro) {
        return formatarData(dtRegistro.toLocalDate());

    }
}
