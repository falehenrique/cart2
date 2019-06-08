package br.com.exmart.rtdpjlite.vo;

import java.io.Serializable;

public class ParteVO implements Serializable {

    private String nome;
    private String documento;
    private String qualidade;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getQualidade() {
        return qualidade;
    }

    public void setQualidade(String qualidade) {
        this.qualidade = qualidade;
    }
}
