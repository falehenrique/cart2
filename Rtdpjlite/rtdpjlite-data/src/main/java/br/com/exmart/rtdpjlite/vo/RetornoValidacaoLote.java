package br.com.exmart.rtdpjlite.vo;

import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.Status;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;

import java.util.List;

public class RetornoValidacaoLote {
    private Cliente cliente;
    private String diretorio;
    private Status statusRegistrado;
    private List<ArquivoCsvImportacao> beans;
    private List<ServicoCalculado> servicosProtocolo;

    public RetornoValidacaoLote(Cliente cliente, String diretorio, Status statusRegistrado, List<ArquivoCsvImportacao> beans, List<ServicoCalculado> servicosProtocolo) {
        this.cliente = cliente;
        this.diretorio = diretorio;
        this.statusRegistrado = statusRegistrado;
        this.beans = beans;
        this.servicosProtocolo = servicosProtocolo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    public Status getStatusRegistrado() {
        return statusRegistrado;
    }

    public void setStatusRegistrado(Status statusRegistrado) {
        this.statusRegistrado = statusRegistrado;
    }

    public List<ArquivoCsvImportacao> getBeans() {
        return beans;
    }

    public void setBeans(List<ArquivoCsvImportacao> beans) {
        this.beans = beans;
    }

    public List<ServicoCalculado> getServicosProtocolo() {
        return servicosProtocolo;
    }

    public void setServicosProtocolo(List<ServicoCalculado> servicosProtocolo) {
        this.servicosProtocolo = servicosProtocolo;
    }
}
