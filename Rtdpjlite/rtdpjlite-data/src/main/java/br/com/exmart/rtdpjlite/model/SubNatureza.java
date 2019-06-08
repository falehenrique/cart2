package br.com.exmart.rtdpjlite.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sub_natureza", schema = "rtdpj")
public class SubNatureza extends AbstractEntity{
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
