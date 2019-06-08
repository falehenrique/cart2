package br.com.exmart.rtdpjlite.model.portal;

import br.com.exmart.rtdpjlite.model.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pedido_protocolo_status", schema = "db_portal")
public class PedidoProtocoloStatus extends AbstractEntity {
    @NotEmpty
    private String nomeAguardando;
    @NotEmpty
    private String nome;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime data;
    private Long numero;
    private String mensagem;
    @NotEmpty
    @Pattern(regexp = "CARTORIO|PARCEIRO|CLIENTE", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String remetente;
    private String arquivo;

    private int nrOrdem;

    public PedidoProtocoloStatus(String nomeAguardando, String nome, LocalDateTime data, Long numero, String mensagem, String remetente, String arquivo, int nrOrdem) {
        this.nomeAguardando = nomeAguardando;
        this.nome = nome;
        this.data = data;
        this.numero = numero;
        this.mensagem = mensagem;
        this.remetente = remetente;
        this.arquivo = arquivo;
        this.nrOrdem = nrOrdem;
    }

    public PedidoProtocoloStatus() {
    }

    public String getNomeAguardando() {
        return nomeAguardando;
    }

    public void setNomeAguardando(String nomeAguardando) {
        this.nomeAguardando = nomeAguardando;
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

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public int getNrOrdem() {
        return nrOrdem;
    }

}
