package br.com.exmart.rtdpjlite.vo.balcaoonline;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Long id;
    private Apresentante apresentante;
    private LocalDateTime data;
    private String arquivo;
    private String observacao;
    private Long protocolo;
    private Long numeroRegistro;
    private String registro;
    private String tipo;
    private String ultimoStatus;
    private List<PedidoMensagem> mensagem;

    public Pedido() {
    }

    public Pedido(Long id, Apresentante apresentante, LocalDateTime data, String arquivo, String observacao, Long protocolo, Long numeroRegistro, String registro, String tipo, List<PedidoMensagem> mensagem) {
        this.id = id;
        this.apresentante = apresentante;
        this.data = data;
        this.arquivo = arquivo;
        this.observacao = observacao;
        this.protocolo = protocolo;
        this.numeroRegistro = numeroRegistro;
        this.registro = registro;
        this.tipo = tipo;
        this.mensagem = mensagem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Apresentante getApresentante() {
        return apresentante;
    }

    public void setApresentante(Apresentante apresentante) {
        this.apresentante = apresentante;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Long protocolo) {
        this.protocolo = protocolo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<PedidoMensagem> getMensagem() {
        if(mensagem == null){
            this.mensagem = new ArrayList<>();
        }
        return mensagem;
    }

    public void setMensagem(List<PedidoMensagem> mensagem) {
        this.mensagem = mensagem;
    }

    public String getUltimoStatus() {
        return ultimoStatus;
    }

    public void setUltimoStatus(String ultimoStatus) {
        this.ultimoStatus = ultimoStatus;
    }
}
