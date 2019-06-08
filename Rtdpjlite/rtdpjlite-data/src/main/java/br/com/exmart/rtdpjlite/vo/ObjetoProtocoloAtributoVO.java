package br.com.exmart.rtdpjlite.vo;

import java.io.Serializable;

public class ObjetoProtocoloAtributoVO  implements Serializable {
    private Long id;
    private String nome;
    private String valor;


    public ObjetoProtocoloAtributoVO() {
    }

    public ObjetoProtocoloAtributoVO(Long id, String nome, String valor) {
        this.id = id;
        this.nome = nome != null? nome.toUpperCase():nome;
        this.valor = valor != null? valor.toUpperCase():valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
