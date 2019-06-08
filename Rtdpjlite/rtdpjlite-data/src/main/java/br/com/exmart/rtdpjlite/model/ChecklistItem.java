package br.com.exmart.rtdpjlite.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "checklist_item")
@Table(schema = "rtdpj")
public class ChecklistItem extends AbstractEntity{
    @NotEmpty
    @Column(length=10485760)
    private String nome;
    @Column(name = "texto_devolucao", length=10485760)
    private String textoDevolucao;

    @Column(name = "obrigatorio")
    private boolean obrigatorio = false;
    @Transient
    private boolean checkado;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public boolean isCheckado() {
        return checkado;
    }

    public void setCheckado(boolean checkado) {
        this.checkado = checkado;
    }

    public String getTextoDevolucao() {
        return textoDevolucao;
    }

    public void setTextoDevolucao(String textoDevolucao) {
        this.textoDevolucao = textoDevolucao;
    }

    @Override
    public String toString() {
        return "<br>"+this.textoDevolucao;
    }
}
