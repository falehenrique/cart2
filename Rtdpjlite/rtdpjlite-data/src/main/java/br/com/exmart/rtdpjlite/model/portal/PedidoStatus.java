package br.com.exmart.rtdpjlite.model.portal;

import br.com.exmart.rtdpjlite.model.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pedido_status", schema = "db_portal")
public class PedidoStatus extends AbstractEntity {
    @NotEmpty
    private String nomeAguardando;
    @NotEmpty
    private String nome;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime data;
    private Long numero;
    private String mensagem;
    private Long cartorioParceiroId;
    private String cartorioParceiroNome;
    private int nrOrdem;

    public PedidoStatus(String nomeAguardando, String nome, LocalDateTime data, Long numero, String mensagem, Long cartorioParceiroId, String cartorioParceiroNome, int nrOrdem) {
        this.nomeAguardando = nomeAguardando;
        this.nome = nome;
        this.data = data;
        this.numero = numero;
        this.mensagem = mensagem;
        this.cartorioParceiroId = cartorioParceiroId;
        this.cartorioParceiroNome = cartorioParceiroNome;
        this.nrOrdem = nrOrdem;
    }

    public PedidoStatus() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNomeAguardando() {
        return nomeAguardando;
    }

    public void setNomeAguardando(String nomeAguardando) {
        this.nomeAguardando = nomeAguardando;
    }

    public Long getCartorioParceiroId() {
        return cartorioParceiroId;
    }

    public void setCartorioParceiroId(Long cartorioParceiroId) {
        this.cartorioParceiroId = cartorioParceiroId;
    }

    public String getCartorioParceiroNome() {
        return cartorioParceiroNome;
    }

    public void setCartorioParceiroNome(String cartorioParceiroNome) {
        this.cartorioParceiroNome = cartorioParceiroNome;
    }

    public int getNrOrdem() {
        return nrOrdem;
    }
}
