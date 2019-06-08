package br.com.exmart.rtdpjlite.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity()
@Table(name="objeto_atributo_opcao", schema = "rtdpj")
public class ObjetoAtributoOpcao extends AbstractEntity {
    private String nome;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
