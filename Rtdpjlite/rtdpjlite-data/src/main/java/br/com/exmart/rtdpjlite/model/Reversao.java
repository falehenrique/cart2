package br.com.exmart.rtdpjlite.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity()
@Table(name = "reversao", schema = "rtdpj")
public class Reversao extends AbstractEntity{

    @Column(name="usuario_id")
    private Long usuarioId;
    @Column(name="data")
    private LocalDateTime data;
    @Column(name="motivo")
    private String motivo;


    @Column(name="protocolo_id")
    private Long protocoloId;
    @Column(name="protocolo_tipo_id")
    private Integer protocoloTipoId;
    @Column(name = "numero_registro")
    private Long numeroRegistro;

    public Reversao() {
    }


    public Reversao(Long usuarioId, LocalDateTime data, String motivo, Long protocoloId, Integer protocoloTipoId, Long numeroRegistro) {
        this.usuarioId = usuarioId;
        this.data = data;
        this.motivo = motivo;
        this.protocoloId = protocoloId;
        this.protocoloTipoId = protocoloTipoId;
        this.numeroRegistro = numeroRegistro;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Long getProtocoloId() {
        return protocoloId;
    }

    public void setProtocoloId(Long protocoloId) {
        this.protocoloId = protocoloId;
    }

    public Integer getProtocoloTipoId() {
        return protocoloTipoId;
    }

    public void setProtocoloTipoId(Integer protocoloTipoId) {
        this.protocoloTipoId = protocoloTipoId;
    }

    public Long getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(Long numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }
}
