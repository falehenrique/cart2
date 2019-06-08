package br.com.exmart.rtdpjlite.vo.financeiro;

import br.com.exmart.rtdpjlite.util.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class CustasCartorio {

    private Long idCustas;
    private Integer numero;
    private String nome;
    @JsonIgnore
    private BigDecimal valor = BigDecimal.ZERO;

    public CustasCartorio() {
    }

    public CustasCartorio(Long idCustas, Integer numero, String nome, BigDecimal valor) {
        this.idCustas = idCustas;
        this.numero = numero;
        this.nome = nome;
        this.valor = valor;
    }

    public Long getIdCustas() {
        return idCustas;
    }

    public void setIdCustas(Long idCustas) {
        this.idCustas = idCustas;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
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
