package br.com.exmart.rtdpjlite.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Heryk on 07/11/17.
 */
@Entity
@Table(name = "cargo", schema = "rtdpj")
public class Cargo extends AbstractEntity {
    @NotEmpty
    private String cargo;


    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return this.cargo;
    }
}
