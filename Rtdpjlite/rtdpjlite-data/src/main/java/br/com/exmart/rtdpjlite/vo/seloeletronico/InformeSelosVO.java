package br.com.exmart.rtdpjlite.vo.seloeletronico;

import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.util.List;

public class InformeSelosVO {

    private String data;
    private String especialidade;
    private Integer protocolo;
    private Integer registro;
    private String selo;
    private BigDecimal valor;
    private String cia;
    private String retificar;
    private List<String> erros;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public Integer getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Integer protocolo) {
        this.protocolo = protocolo;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getSelo() {
        return selo;
    }

    public void setSelo(String selo) {
        this.selo = selo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCia() {
        return cia;
    }

    public void setCia(String cia) {
        this.cia = cia;
    }

    public String getRetificar() {
        return retificar;
    }

    public void setRetificar(String retificar) {
        this.retificar = retificar;
    }

    public List<String> getErros() {
        return erros;
    }

    public void setErros(List<String> erros) {
        this.erros = erros;
    }

    public String listarErros() {
        String retorno = "";
        for(String erro: erros){
            if(Strings.isNullOrEmpty(retorno)){
                retorno = retorno + erro;
            }else{
                retorno = retorno + ", \n" + erro;
            }
        }
        return retorno;
    }
}
