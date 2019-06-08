package br.com.exmart.indicadorRTDPJ.ui.component;

import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.TextField;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by fabioebner on 28/05/17.
 */
public class MyTextField extends TextField {
    private Tipos tipoMascara = Tipos.NORMAL;
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    DecimalFormat decimalFormat = null;
    boolean icVazio = false;
    private Locale ptBr = new Locale("pt", "BR");

    public void setIcVazio(boolean icVazio) {
        this.icVazio = icVazio;
    }

    private void inicializacao(){
        super.setValueChangeMode(ValueChangeMode.EAGER);
        super.addValueChangeListener(event -> adicionarMascara(event));
        String pattern = "#,##0.00";
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
    }

    public void setTipoMascara(Tipos tipoMascara) {
        inicializacao();
        this.tipoMascara = tipoMascara;
    }

    private void adicionarMascara(ValueChangeEvent<String> event) {
        if(tipoMascara.equals(Tipos.DATA)){
            System.out.println(event.getValue());
        }else if(tipoMascara.equals(Tipos.VALOR)){
            String valor = event.getValue().replaceAll("[^0-9]", "");//event.getText().replace(".","");
            if(valor.length() == 0){
                valor = "000";
            }
            valor = Long.valueOf(valor).toString();
            if(valor.length() == 1){
                valor = "0.0"+valor;
            }else if(valor.length() == 2){
                valor = "0."+valor;
            }else{
                String novoValor = valor.substring(0,valor.length()-2)+"."+valor.substring(valor.length()-2,valor.length());
                valor = novoValor;
            }

            this.setValue(decimalFormat.format(new BigDecimal(valor)));
            if(this.icVazio){
                if(new BigDecimal(valor).signum() == 0){
                    this.clear();
                }
            }
        }
    }



    public enum Tipos {
        DATA,
        VALOR,
        NORMAL;
    }
}
