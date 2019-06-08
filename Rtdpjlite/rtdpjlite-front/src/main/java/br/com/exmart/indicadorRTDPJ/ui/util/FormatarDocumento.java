package br.com.exmart.indicadorRTDPJ.ui.util;

import br.com.caelum.stella.format.CNPJFormatter;
import br.com.caelum.stella.format.CPFFormatter;
import com.google.common.base.Strings;

import java.text.SimpleDateFormat;

/**
 * Created by fabioebner on 12/07/17.
 */
public  class FormatarDocumento {
    static CPFFormatter cpfFormatter = new CPFFormatter();
    static CNPJFormatter cnpjFormatter = new CNPJFormatter();
    static CpfCnpjValidator cpfCnpjValidator = new CpfCnpjValidator();
    static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static String formataDocumento(String documento) {
        if(!Strings.isNullOrEmpty(documento)) {
            String value = documento.replace(".","").replace("-","").replace("\\","");
            if (cpfFormatter.canBeFormatted(value)) {
                return cpfFormatter.format(value);
            } else if (cnpjFormatter.canBeFormatted(value)) {
                return cnpjFormatter.format(value);
            }
        }
        return "";
    }

    public static boolean validarCpfCnpj(String documento){
        return cpfCnpjValidator.isValid(documento, null);
    }

    public static boolean isCpf(String documento){
        try {
            return cpfFormatter.canBeFormatted(cpfFormatter.unformat(documento));
        }catch (IllegalArgumentException iae){
            return false;
        }
    }


}
