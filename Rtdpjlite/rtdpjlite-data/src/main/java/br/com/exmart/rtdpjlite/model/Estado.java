package br.com.exmart.rtdpjlite.model;

import javax.persistence.*;
import java.util.List;

@Entity()
@Table(name = "estado", schema = "rtdpj")
//@NamedEntityGraphs({@NamedEntityGraph( name = "Estado.cidades", attributeNodes = @NamedAttributeNode("cidades"))})
public class Estado extends AbstractEntity {
    private String nome;
    private String sg;
    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name="estado_id")
    private List<Cidade> cidades;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSg() {
        return sg;
    }

    public void setSg(String sg) {
        this.sg = sg;
    }

    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }

    @Override
    public String toString() {
        return sg;
    }
}
