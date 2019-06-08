package br.com.exmart.indicadorRTDPJ.service.rest.vo;

public class FormaPagamentoVO {
    String nome;
    boolean credito = false;

    public FormaPagamentoVO() {
    }

    public FormaPagamentoVO(String nome, boolean credito) {
        this.nome = nome;
        this.credito = credito;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isCredito() {
        return credito;
    }

    public void setCredito(boolean credito) {
        this.credito = credito;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
