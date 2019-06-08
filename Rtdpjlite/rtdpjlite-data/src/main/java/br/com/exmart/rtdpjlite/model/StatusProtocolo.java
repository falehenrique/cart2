package br.com.exmart.rtdpjlite.model;

import br.com.exmart.rtdpjlite.model.custom.JsonStatusProtocoloUserType;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Table(name="status_protocolo", schema = "rtdpj")
@Entity()
@TypeDef(name = "JsonStatusProtocoloUserType", typeClass = JsonStatusProtocoloUserType.class)
public class StatusProtocolo extends  AbstractEntity{

    @OneToOne(fetch= FetchType.EAGER)
    @NotNull
    private Status status;
    @OneToOne(fetch= FetchType.EAGER)
    @NotNull
    @JsonIgnore
    private Usuario usuario;
    @NotNull
    private LocalDateTime data;
//    private String conteudo;
    //TODO deve colocar usuario

    @Transient
    @JsonIgnore
    private byte[] arquivo = null;
    @Type(type = "JsonStatusProtocoloUserType")
    @JsonIgnore
    private StatusProtocoloJson conteudo;


    public StatusProtocolo() {
    }

    public StatusProtocolo(Status status, LocalDateTime data, Usuario usuario) {
        this.status = status;
        this.data = data;
        this.usuario = usuario;
    }

    public StatusProtocolo(Status status, LocalDateTime data, byte[] arquivo, StatusProtocoloJson conteudo, Usuario usuario) {
        this.status = status;
        this.data = data;
        this.conteudo = conteudo;
        this.arquivo = arquivo;
        if(conteudo != null) {
            conteudo.setPossuiAnexo(arquivo != null);
        }
        this.usuario = usuario;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return status.getNome();
    }

    public StatusProtocoloJson getConteudo() {
        return conteudo;
    }

    public void setConteudo(StatusProtocoloJson conteudo) {
        this.conteudo = conteudo;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
