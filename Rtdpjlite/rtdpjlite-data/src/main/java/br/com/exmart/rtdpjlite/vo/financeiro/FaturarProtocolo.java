package br.com.exmart.rtdpjlite.vo.financeiro;

public class FaturarProtocolo {
    Long nrProtocolo;
    String nomeCliente;
    String documentoCliente;
    String nmFantasiaCliente;
    String apresentante;
    Long idUsuario;
    String natureza;
    boolean icCertidao;

    public FaturarProtocolo(Long nrProtocolo, String nomeCliente, String documentoCliente, String nmFantasiaCliente, String apresentante, Long idUsuario, String natureza, boolean icCertidao) {
        this.nrProtocolo = nrProtocolo;
        this.nomeCliente = nomeCliente;
        this.documentoCliente = documentoCliente;
        this.nmFantasiaCliente = nmFantasiaCliente;
        this.apresentante = apresentante;
        this.idUsuario = idUsuario;
        this.natureza = natureza;
        this.icCertidao = icCertidao;
    }

    public Long getNrProtocolo() {
        return nrProtocolo;
    }

    public void setNrProtocolo(Long nrProtocolo) {
        this.nrProtocolo = nrProtocolo;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getDocumentoCliente() {
        return documentoCliente;
    }

    public void setDocumentoCliente(String documentoCliente) {
        this.documentoCliente = documentoCliente;
    }

    public String getNmFantasiaCliente() {
        return nmFantasiaCliente;
    }

    public void setNmFantasiaCliente(String nmFantasiaCliente) {
        this.nmFantasiaCliente = nmFantasiaCliente;
    }

    public String getApresentante() {
        return apresentante;
    }

    public void setApresentante(String apresentante) {
        this.apresentante = apresentante;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public boolean isIcCertidao() {
        return icCertidao;
    }

    public void setIcCertidao(boolean icCertidao) {
        this.icCertidao = icCertidao;
    }
}
