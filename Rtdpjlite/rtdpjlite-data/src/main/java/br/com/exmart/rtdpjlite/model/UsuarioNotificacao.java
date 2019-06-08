package br.com.exmart.rtdpjlite.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_notificacao", schema = "rtdpj")
public class UsuarioNotificacao extends AbstractEntity {

    @Column(name = "usuario_id")
    private Long usuarioId;
    @NotNull
    private LocalDateTime criada;
    private LocalDateTime lida;
    @NotEmpty
    private String assunto;
    @Column(length=10485760)
    @NotEmpty
    private String descricao;


    public UsuarioNotificacao(Long usuarioId, LocalDateTime criada, String assunto, String descricao) {
        this.usuarioId = usuarioId;
        this.criada = criada;
        this.assunto = assunto;
        this.descricao = descricao;
    }

    public UsuarioNotificacao() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDateTime getCriada() {
        return criada;
    }

    public void setCriada(LocalDateTime criada) {
        this.criada = criada;
    }

    public LocalDateTime getLida() {
        return lida;
    }

    public void setLida(LocalDateTime lida) {
        this.lida = lida;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
