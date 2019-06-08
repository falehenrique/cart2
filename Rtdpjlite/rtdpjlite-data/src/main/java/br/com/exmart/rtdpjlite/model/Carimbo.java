package br.com.exmart.rtdpjlite.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "carimbo", schema = "rtdpj")
public class Carimbo extends AbstractEntity{
    @NotEmpty
    private String nome;
    @NotEmpty
    @Column(length=10485760)
    private String carimbo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCarimbo() {
        return carimbo;
    }

    public void setCarimbo(String carimbo) {
        this.carimbo = carimbo;
    }
}
