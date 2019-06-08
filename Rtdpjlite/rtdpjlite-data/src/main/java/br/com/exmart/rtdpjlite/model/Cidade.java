package br.com.exmart.rtdpjlite.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity()
@Table(schema = "rtdpj", name = "cidade")
public class Cidade extends  AbstractEntity{
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
