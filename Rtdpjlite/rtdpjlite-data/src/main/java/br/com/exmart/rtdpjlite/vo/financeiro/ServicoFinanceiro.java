package br.com.exmart.rtdpjlite.vo.financeiro;

public class ServicoFinanceiro {
    private Long idServico;
    private String nmServico;
    private String sgTabelaCusta;
    private Long cdCartorioNatureza;
    private boolean icDepositoPrevio = false;
    private boolean icQuantidadeExcedente = false;
    private String nmQuantidadeExcedente;
    private boolean icCertidao = false;
    private boolean nmCategoria;

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

    public String getSgTabelaCusta() {
        return sgTabelaCusta;
    }

    public void setSgTabelaCusta(String sgTabelaCusta) {
        this.sgTabelaCusta = sgTabelaCusta;
    }

    public Long getCdCartorioNatureza() {
        return cdCartorioNatureza;
    }

    public void setCdCartorioNatureza(Long cdCartorioNatureza) {
        this.cdCartorioNatureza = cdCartorioNatureza;
    }

    public boolean isIcDepositoPrevio() {
        return icDepositoPrevio;
    }

    public void setIcDepositoPrevio(boolean icDepositoPrevio) {
        this.icDepositoPrevio = icDepositoPrevio;
    }

    public boolean isIcQuantidadeExcedente() {
        return icQuantidadeExcedente;
    }

    public void setIcQuantidadeExcedente(boolean icQuantidadeExcedente) {
        this.icQuantidadeExcedente = icQuantidadeExcedente;
    }

    public String getNmQuantidadeExcedente() {
        return nmQuantidadeExcedente;
    }

    public void setNmQuantidadeExcedente(String nmQuantidadeExcedente) {
        this.nmQuantidadeExcedente = nmQuantidadeExcedente;
    }

    public boolean isIcCertidao() {
        return icCertidao;
    }

    public void setIcCertidao(boolean icCertidao) {
        this.icCertidao = icCertidao;
    }

    public boolean isNmCategoria() {
        return nmCategoria;
    }

    public void setNmCategoria(boolean nmCategoria) {
        this.nmCategoria = nmCategoria;
    }
}
