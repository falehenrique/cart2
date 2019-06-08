package br.com.exmart.rtdpjlite.model;

import br.com.exmart.rtdpjlite.model.custom.TipoModeloConverter;
import br.com.exmart.rtdpjlite.model.custom.TipoProtocoloConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity()
@Table(name = "modelo", schema = "rtdpj")
public class Modelo extends AbstractEntity{
    public String nome;
    
    @Column(length=10485760)
    public String modelo;
    
    @Column(name = "tipo_modelo_id")
    @Convert(converter = TipoModeloConverter.class)
    @NotNull
    private TipoModelo tipo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public TipoModelo getTipo() {
        return tipo;
    }

    public void setTipo(TipoModelo tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
