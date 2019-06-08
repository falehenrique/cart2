package br.com.exmart.rtdpjlite.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "checklist_grupo", schema = "rtdpj")
public class ChecklistGrupo extends AbstractEntity{
    @NotEmpty
    private String nome;

    @OneToMany(cascade = CascadeType.ALL,fetch= FetchType.EAGER)
    @JoinColumn(name="checklist_grupo_id")
    private List<ChecklistItem> itens;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ChecklistItem> getItens() {
        return itens;
    }

    public void setItens(List<ChecklistItem> itens) {
        this.itens = itens;
    }
}
