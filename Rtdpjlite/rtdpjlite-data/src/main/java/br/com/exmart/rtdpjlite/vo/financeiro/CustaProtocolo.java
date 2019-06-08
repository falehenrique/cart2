package br.com.exmart.rtdpjlite.vo.financeiro;

import br.com.exmart.rtdpjlite.util.Utils;

import java.math.BigDecimal;

public class CustaProtocolo {
    private String nome;
    private BigDecimal valor = BigDecimal.ZERO;

    public CustaProtocolo(String nome, BigDecimal valor) {
        this.nome = nome;
        this.valor= valor;
    }

    public CustaProtocolo() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }


    @Override
    public String toString() {
        return nome + " R$: " + Utils.formataValor(this.valor);
    }
}
