package br.com.exmart.rtdpjlite.service.filter;

import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.Natureza;
import br.com.exmart.rtdpjlite.model.Status;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProtocoloFiltro {
    private LocalDateTime dtInicial;
    private LocalDateTime dtFinal;
    private Long numero;
    private Status andamento;
    private Natureza natureza;
    private String campoData;
    private Cliente cliente;
    private String buscaTexto;

    private TipoProtocolo tipoProtocolo;

    public ProtocoloFiltro(LocalDate dtInicial, LocalDate dtFinal, Long numero, Status andamento, Natureza natureza, String campoData, TipoProtocolo tipoProtocolo, Cliente cliente, String buscaTexto) {
        if(dtInicial != null)
        this.dtInicial = dtInicial.atTime(12,12,12);
        if(dtFinal != null)
        this.dtFinal = dtFinal.atTime(12,12,12);;
        this.numero = numero;
        this.andamento = andamento;
        this.natureza = natureza;
        this.campoData = campoData;
        this.tipoProtocolo = tipoProtocolo;
        this.cliente = cliente;
        this.buscaTexto = buscaTexto;
    }

    public LocalDateTime getDtInicial() {
        return dtInicial;
    }

    public void setDtInicial(LocalDateTime dtInicial) {
        this.dtInicial = dtInicial;
    }

    public LocalDateTime getDtFinal() {
        return dtFinal;
    }

    public void setDtFinal(LocalDateTime dtFinal) {
        this.dtFinal = dtFinal;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Status getAndamento() {
        return andamento;
    }

    public void setAndamento(Status andamento) {
        this.andamento = andamento;
    }

    public Natureza getNatureza() {
        return natureza;
    }

    public void setNatureza(Natureza natureza) {
        this.natureza = natureza;
    }

    public String getCampoData() {
        return campoData;
    }

    public void setCampoData(String campoData) {
        this.campoData = campoData;
    }

    public TipoProtocolo getTipoProtocolo() {
        return tipoProtocolo;
    }

    public void setTipoProtocolo(TipoProtocolo tipoProtocolo) {
        this.tipoProtocolo = tipoProtocolo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getBuscaTexto() {
        return buscaTexto;
    }

    public void setBuscaTexto(String buscaTexto) {
        this.buscaTexto = buscaTexto;
    }
}
