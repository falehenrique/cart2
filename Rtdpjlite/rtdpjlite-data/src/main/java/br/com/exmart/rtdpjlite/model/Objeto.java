package br.com.exmart.rtdpjlite.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity()
@Table(name = "objeto", schema = "rtdpj")
//@NamedEntityGraphs({
//        @NamedEntityGraph( name = "Objeto.full", attributeNodes = {@NamedAttributeNode(value = "atributos", subgraph = "FetchManagers.Subgraph.Opcao")},
//        subgraphs = {
//                @NamedSubgraph(name = "FetchManagers.Subgraph.Opcao", type = ObjetoAtributo.class, attributeNodes = {@NamedAttributeNode(value = "opcoes")})
//        })
//})
public class Objeto extends AbstractEntity {
    @NotEmpty
    private String nome;
    private boolean icGarantia;

    @OneToMany(cascade = CascadeType.ALL,fetch= FetchType.EAGER)
    @JoinColumn(name="objeto_id")
    private List<ObjetoAtributo> atributos;

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

    public List<ObjetoAtributo> getAtributos() {
        if(atributos == null){
            this.atributos = new ArrayList<>();
        }
        return atributos;
    }

    public void setAtributos(List<ObjetoAtributo> atributos) {
        this.atributos = atributos;
    }

    public boolean isIcGarantia() {
        return icGarantia;
    }

    public void setIcGarantia(boolean icGarantia) {
        this.icGarantia = icGarantia;
    }

}
