package br.com.exmart.rtdpjlite.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lote_item", schema = "rtdpj")
public class LoteItem extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name="lote_id")
    private Lote lote;
    private LocalDateTime inicio;
    private Long protocoloId;
    @Column(name = "nome_ignorar")
    private String nomeIgnorar;

    private LocalDateTime dtProtocolo;
    private LocalDateTime dtRegistro;
    private LocalDateTime dtFaturado;

    private String termoAbertura;
    private String arquivo;
    private String termoEncerramento;

    private LocalDateTime finalizado;
    private String resultado;

    public LoteItem() {
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public Long getProtocoloId() {
        return protocoloId;
    }

    public void setProtocoloId(Long protocoloId) {
        this.protocoloId = protocoloId;
    }

    public LocalDateTime getDtProtocolo() {
        return dtProtocolo;
    }

    public void setDtProtocolo(LocalDateTime dtProtocolo) {
        this.dtProtocolo = dtProtocolo;
    }

    public LocalDateTime getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(LocalDateTime dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public LocalDateTime getDtFaturado() {
        return dtFaturado;
    }

    public void setDtFaturado(LocalDateTime dtFaturado) {
        this.dtFaturado = dtFaturado;
    }

    public String getTermoAbertura() {
        return termoAbertura;
    }

    public void setTermoAbertura(String termoAbertura) {
        this.termoAbertura = termoAbertura;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getTermoEncerramento() {
        return termoEncerramento;
    }

    public void setTermoEncerramento(String termoEncerramento) {
        this.termoEncerramento = termoEncerramento;
    }

    public LocalDateTime getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(LocalDateTime finalizado) {
        this.finalizado = finalizado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public String getNomeIgnorar() {
        return nomeIgnorar;
    }

    public void setNomeIgnorar(String nomeIgnorar) {
        this.nomeIgnorar = nomeIgnorar;
    }
}
