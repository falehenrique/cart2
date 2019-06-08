package br.com.exmart.rtdpjlite.vo;

import java.io.Serializable;

public class ObjetoProtocoloParteVO implements Serializable{
    private String documento;
    private Float fracao;

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Float getFracao() {
        return fracao;
    }

    public void setFracao(Float fracao) {
        this.fracao = fracao;
    }
}
