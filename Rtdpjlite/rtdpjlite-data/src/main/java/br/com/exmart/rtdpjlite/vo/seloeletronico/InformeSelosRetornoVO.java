package br.com.exmart.rtdpjlite.vo.seloeletronico;

import java.util.List;

public class InformeSelosRetornoVO {
    private String selo;
    private Integer totalErros;
    private List<String> erros;


    public String getSelo() {
        return selo;
    }

    public void setSelo(String selo) {
        this.selo = selo;
    }

    public Integer getTotalErros() {
        return totalErros;
    }

    public void setTotalErros(Integer totalErros) {
        this.totalErros = totalErros;
    }

    public List<String> getErros() {
        return erros;
    }

    public void setErros(List<String> erros) {
        this.erros = erros;
    }
}
