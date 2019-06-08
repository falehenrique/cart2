package br.com.exmart.rtdpjlite.vo.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class PedidoFinanceiro implements Serializable {

    private Long protocolo;
    private String natureza;
    private String apresentante;
    private String apresentanteDocumento;
    private String idAtendimento;
    private boolean certidao;
    private BigDecimal vlDepositoPrevio;
    List<ServicoCalculado> servicos;


    public PedidoFinanceiro() {
    }

    public PedidoFinanceiro(Long protocoloNumero, String natureza, String apresentante, String apresentanteRg, String idAtendimento, boolean certidao, List<ServicoCalculado> servicos, BigDecimal vlDepositoPrevio) {
        this.protocolo = protocoloNumero;
        this.natureza = natureza;
        this.apresentante = apresentante;
        this.apresentanteDocumento = apresentanteRg;
        this.idAtendimento = idAtendimento;
        this.certidao = certidao;
        this.servicos = servicos;
        this.vlDepositoPrevio = vlDepositoPrevio;
    }

    public Long getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Long protocolo) {
        this.protocolo = protocolo;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public String getApresentante() {
        return apresentante;
    }

    public void setApresentante(String apresentante) {
        this.apresentante = apresentante;
    }

    public String getApresentanteDocumento() {
        return apresentanteDocumento;
    }

    public void setApresentanteDocumento(String apresentanteDocumento) {
        this.apresentanteDocumento = apresentanteDocumento;
    }

    public String getIdAtendimento() {
        return idAtendimento;
    }

    public void setIdAtendimento(String idAtendimento) {
        this.idAtendimento = idAtendimento;
    }

    public boolean isCertidao() {
        return certidao;
    }

    public void setCertidao(boolean certidao) {
        this.certidao = certidao;
    }

    public List<ServicoCalculado> getServicos() {
        return servicos;
    }

    public void setServicos(List<ServicoCalculado> servicos) {
        this.servicos = servicos;
    }

    public BigDecimal getVlDepositoPrevio() {
        return vlDepositoPrevio;
    }

    public void setVlDepositoPrevio(BigDecimal vlDepositoPrevio) {
        this.vlDepositoPrevio = vlDepositoPrevio;
    }
}
