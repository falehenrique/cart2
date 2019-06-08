package br.com.exmart.indicadorRTDPJ.ui.vo;

import java.math.BigDecimal;

public class ServicosAgrupadoVO {
    private Long idServico;
    private String nmServico;
    private BigDecimal total;
    private BigDecimal vlBase;
    private String formaCalculo;
    private int qtd;

    public ServicosAgrupadoVO(Long idServico, String nmServico, BigDecimal total, BigDecimal vlBase, String formaCalculo, int qtd) {
        this.idServico = idServico;
        this.nmServico = nmServico;
        this.total = total;
        this.vlBase = vlBase;
        this.formaCalculo = formaCalculo;
        this.qtd = qtd;
    }

    public ServicosAgrupadoVO() {
    }

    public Long getIdServico() {
        return idServico;
    }

    public void setIdServico(Long idServico) {
        this.idServico = idServico;
    }

    public String getNmServico() {
        return nmServico;
    }

    public void setNmServico(String nmServico) {
        this.nmServico = nmServico;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getVlBase() {
        return vlBase;
    }

    public void setVlBase(BigDecimal vlBase) {
        this.vlBase = vlBase;
    }

    public String getFormaCalculo() {
        return formaCalculo;
    }

    public void setFormaCalculo(String formaCalculo) {
        this.formaCalculo = formaCalculo;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
}
