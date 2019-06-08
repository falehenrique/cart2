package br.com.exmart.indicadorRTDPJ.service.rest.vo;

import java.util.Date;

/**
 * Created by fabioebner on 28/05/17.
 */
public class ProtocoloAndamentoVO {
    private Long id;
    private Date data;
    private String usuario;
    private String nome;
    private Long idUsuario;
    private Long idProtocolo;
    private Long idProtocoloTipo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdProtocolo() {
        return idProtocolo;
    }

    public void setIdProtocolo(Long idProtocolo) {
        this.idProtocolo = idProtocolo;
    }

    public Long getIdProtocoloTipo() {
        return idProtocoloTipo;
    }

    public void setIdProtocoloTipo(Long idProtocoloTipo) {
        this.idProtocoloTipo = idProtocoloTipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
