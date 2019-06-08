package br.com.exmart.rtdpjlite.vo.balcaoonline;

import com.google.common.base.Strings;

import java.time.LocalDateTime;

public class PedidoMensagem {
    //POST /pedido/1/mensagem
    private Long id;
    private PedidoMensagemTipo tipo;
    private PedidoMensagemRemetente remetente;
    private PedidoMensagemRemetente destinatario;
    private LocalDateTime data;
    private String conteudo;
    private String hash;
    private String arquivo;

    public PedidoMensagemTipo getTipo() {
        return tipo;
    }

    public void setTipo(PedidoMensagemTipo tipo) {
        this.tipo = tipo;
    }

    public PedidoMensagemRemetente getRemetente() {
        return remetente;
    }

    public void setRemetente(PedidoMensagemRemetente remetente) {
        this.remetente = remetente;
    }

    public PedidoMensagemRemetente getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(PedidoMensagemRemetente destinatario) {
        this.destinatario = destinatario;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getConteudo() {
        if(Strings.isNullOrEmpty(conteudo)){
            conteudo = "";
        }
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
