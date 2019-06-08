package br.com.exmart.rtdpjlite.model;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Heryk on 07/11/17.
 */
@Entity()
@Table(name = "usuario_dados", schema = "rtdpj")
public class UsuarioDados extends AbstractEntity {

    @NotEmpty
    Integer id_usuario;

    @NotEmpty
    String nome;

    @NotEmpty
    Integer id_cargo;

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getId_cargo() {
        return id_cargo;
    }

    public void setId_cargo(Integer id_cargo) {
        this.id_cargo = id_cargo;
    }
}
