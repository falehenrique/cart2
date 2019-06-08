package br.com.exmart.rtdpjlite.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity()
@Table(name = "objeto_atributo", schema = "rtdpj")
public class ObjetoAtributo extends AbstractEntity {
    private String nome;
    @Column(name = "obrigatorio")
    private boolean obrigatorio = false;
    @OneToMany(cascade = CascadeType.ALL,fetch= FetchType.EAGER)
    @JoinColumn(name="objeto_atributo_id")
    Set<ObjetoAtributoOpcao> opcoes;
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<ObjetoAtributoOpcao> getOpcoes() {
        if(opcoes == null){
            this.opcoes = new HashSet<>();
        }
        return opcoes;
    }

    public void setOpcoes(Set<ObjetoAtributoOpcao> opcoes) {
        this.opcoes = opcoes;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }
}
