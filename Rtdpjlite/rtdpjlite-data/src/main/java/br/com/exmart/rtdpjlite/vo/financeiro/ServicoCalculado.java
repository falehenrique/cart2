package br.com.exmart.rtdpjlite.vo.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;


public class ServicoCalculado implements Serializable{
    private Long idServico;
    private String nmServico;
    private String nrSelo;
    private Long idServicoCalculado;

    private BigDecimal vlBase = BigDecimal.ZERO;
    private Integer cdFormaCalculo;
    private String nmFormaCalculo;
    private String tabelaCusta;
    private boolean icNecessitaSelo = false;

    private BigDecimal vlDesconto = BigDecimal.ZERO;

    private BigDecimal custas1;
    private BigDecimal custas2;
    private BigDecimal custas3;
    private BigDecimal custas4;
    private BigDecimal custas5;
    private BigDecimal custas6;
    private BigDecimal custas7;
    private BigDecimal custas8;
    private BigDecimal custas9;
    private BigDecimal custas10;


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

    public BigDecimal getVlBase() {
        return vlBase;
    }

    public void setVlBase(BigDecimal vlBase) {
        this.vlBase = vlBase;
    }

    public Integer getCdFormaCalculo() {
        return cdFormaCalculo;
    }

    public void setCdFormaCalculo(Integer cdFormaCalculo) {
        this.cdFormaCalculo = cdFormaCalculo;
    }

    public String getNmFormaCalculo() {
        return nmFormaCalculo;
    }

    public void setNmFormaCalculo(String nmFormaCalculo) {
        this.nmFormaCalculo = nmFormaCalculo;
    }

    public BigDecimal getVlDesconto() {
        return vlDesconto;
    }

    public void setVlDesconto(BigDecimal vlDesconto) {
        this.vlDesconto = vlDesconto;
    }

    public BigDecimal getCustas1() {
        return custas1;
    }

    public void setCustas1(BigDecimal custas1) {
        this.custas1 = custas1;
    }

    public BigDecimal getCustas2() {
        return custas2;
    }

    public void setCustas2(BigDecimal custas2) {
        this.custas2 = custas2;
    }

    public BigDecimal getCustas3() {
        return custas3;
    }

    public void setCustas3(BigDecimal custas3) {
        this.custas3 = custas3;
    }

    public BigDecimal getCustas4() {
        return custas4;
    }

    public void setCustas4(BigDecimal custas4) {
        this.custas4 = custas4;
    }

    public BigDecimal getCustas5() {
        return custas5;
    }

    public void setCustas5(BigDecimal custas5) {
        this.custas5 = custas5;
    }

    public BigDecimal getCustas6() {
        return custas6;
    }

    public void setCustas6(BigDecimal custas6) {
        this.custas6 = custas6;
    }

    public BigDecimal getCustas7() {
        return custas7;
    }

    public void setCustas7(BigDecimal custas7) {
        this.custas7 = custas7;
    }

    public BigDecimal getCustas8() {
        return custas8;
    }

    public void setCustas8(BigDecimal custas8) {
        this.custas8 = custas8;
    }

    public BigDecimal getCustas9() {
        return custas9;
    }

    public void setCustas9(BigDecimal custas9) {
        this.custas9 = custas9;
    }

    public BigDecimal getCustas10() {
        return custas10;
    }

    public void setCustas10(BigDecimal custas10) {
        this.custas10 = custas10;
    }

    public BigDecimal getTotal(){
        BigDecimal retorno = BigDecimal.ZERO;
        try {
            retorno = retorno.add(this.custas1);
            retorno = retorno.add(this.custas2);
            retorno = retorno.add(this.custas3);
            retorno = retorno.add(this.custas4);
            retorno = retorno.add(this.custas5);
            retorno = retorno.add(this.custas6);
            retorno = retorno.add(this.custas7);
            retorno = retorno.add(this.custas8);
            retorno = retorno.add(this.custas9);
            retorno = retorno.add(this.custas10);
        }catch (NullPointerException e){

        }
        return  retorno;
    }

    public String getNrSelo() {
        return nrSelo;
    }

    public void setNrSelo(String nrSelo) {
        this.nrSelo = nrSelo;
    }

    public Long getIdServicoCalculado() {
        return idServicoCalculado;
    }

    public void setIdServicoCalculado(Long idServicoCalculado) {
        this.idServicoCalculado = idServicoCalculado;
    }

    public boolean isIcNecessitaSelo() {
        return icNecessitaSelo;
    }

    public void setIcNecessitaSelo(boolean icNecessitaSelo) {
        this.icNecessitaSelo = icNecessitaSelo;
    }


    public String getTabelaCusta() {
        return tabelaCusta;
    }

    public void setTabelaCusta(String tabelaCusta) {
        this.tabelaCusta = tabelaCusta;
    }
}
