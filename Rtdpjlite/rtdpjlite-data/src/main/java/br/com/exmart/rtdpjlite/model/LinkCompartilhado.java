package br.com.exmart.rtdpjlite.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "link_compartilhado", schema = "rtdpj")
public class LinkCompartilhado {
    @Id
    @NotEmpty
    private String id;
    @Column(name = "usuario_id")
    private Long usuario;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime data;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name="link_compartilhado_id")
    private List<LinkCompartilhadoRegistro> registros;

    public LinkCompartilhado() {
    }

    public LinkCompartilhado(String id, Long usuario) {
        this.id = id;
        this.usuario = usuario;
        this.data = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public List<LinkCompartilhadoRegistro> getRegistros() {
        if(this.registros == null)
            this.registros = new ArrayList<>();
        return registros;
    }

    public void setRegistros(List<LinkCompartilhadoRegistro> registros) {
        this.registros = registros;
    }
}
