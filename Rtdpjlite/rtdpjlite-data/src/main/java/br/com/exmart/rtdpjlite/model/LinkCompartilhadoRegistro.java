package br.com.exmart.rtdpjlite.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "link_compartilhado_registro", schema = "rtdpj")
public class LinkCompartilhadoRegistro extends AbstractEntity {
    private Long registroId;
    private Long numeroRegistro;
    private String registro;
    private String especialidade;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime data;

    public LinkCompartilhadoRegistro() {
    }

    public LinkCompartilhadoRegistro(Long registroId, Long numeroRegistro, String registro, String especialidade, LocalDateTime data) {
        this.registroId = registroId;
        this.numeroRegistro = numeroRegistro;
        this.registro = registro;
        this.especialidade = especialidade;
        this.data = data;
    }

    public Long getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Long registroId) {
        this.registroId = registroId;
    }

    public Long getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(Long numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
