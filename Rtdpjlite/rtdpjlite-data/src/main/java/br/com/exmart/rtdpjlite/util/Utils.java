package br.com.exmart.rtdpjlite.util;

import br.com.exmart.util.BeanLocator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.money.CurrencyUnit;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Locale;

public class Utils {
    public static final CurrencyUnit BRL = CurrencyUnit.of("BRL");
    static DecimalFormat decimalFormat = null;
    static DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private static Locale ptBr = new Locale("pt", "BR");
    private static String dtFormat= "dd/MM/yyyy";
    private static ObjectMapper jsonConverter;
    static {
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        String pattern = "#,##0.00";
        decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
//        try {
            jsonConverter = BeanLocator.find(ObjectMapper.class);
//        }catch (Exception e){}
    }

    public static String formataValor(BigDecimal valor) {
        return decimalFormat.format(valor);
    }

    public static String formatarData(LocalDate data){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dtFormat);
        return data.format(formatter);
    }
    public static Date localDateToDate(LocalDate localDate){
        if(localDate == null){
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static String formatarData(Date data){
        return dateFormat.format(data);
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
    public static boolean isCnpj(String cnpj) {
        cnpj = cnpj.replace(".", "");
        cnpj = cnpj.replace("-", "");
        cnpj = cnpj.replace("/", "");

        try {
            Long.parseLong(cnpj);
        } catch (NumberFormatException e) {
            return false;
        }

        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111")
                || cnpj.equals("22222222222222") || cnpj.equals("33333333333333")
                || cnpj.equals("44444444444444") || cnpj.equals("55555555555555")
                || cnpj.equals("66666666666666") || cnpj.equals("77777777777777")
                || cnpj.equals("88888888888888")  || (cnpj.length() != 14))//|| cnpj.equals("99999999999999")
            return (false);
        char dig13, dig14;
        int sm, i, r, num, peso; // "try" - protege o código para eventuais
        // erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // converte o i-ésimo caractere do CNPJ em um número: // por
                // exemplo, transforma o caractere '0' no inteiro 0 // (48 eh a
                // posição de '0' na tabela ASCII)
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else
                dig13 = (char) ((11 - r) + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }
            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else
                dig14 = (char) ((11 - r) + 48);
            // Verifica se os dígitos calculados conferem com os dígitos
            // informados.
            if ((dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13)))
                return (true);
            else
                return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }
    public static String formatarData(LocalDateTime dtRegistro) {
        return formatarData(dtRegistro.toLocalDate());
    }

    public static LocalDate dataFromString(String dataDocumento) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dtFormat);
        return LocalDate.parse(dataDocumento,formatter);
    }
    public static String formatarDataComHora(LocalDateTime data){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return data.format(formatter);
    }
}
