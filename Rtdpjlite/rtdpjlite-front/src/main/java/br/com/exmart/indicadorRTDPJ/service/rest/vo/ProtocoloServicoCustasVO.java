package br.com.exmart.indicadorRTDPJ.service.rest.vo;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by fabioebner on 28/05/17.
 */
public class ProtocoloServicoCustasVO {
    private Long id;
    @NotEmpty
    private String tabelaCusta;
    @NotNull
    private BigDecimal vlBase;
    @NotNull
    private int nrQuantidadeBase;
    @NotNull
    private BigDecimal custas1;
    @NotNull
    private BigDecimal custas2;
    @NotNull
    private BigDecimal custas3;
    @NotNull
    private BigDecimal custas4;
    @NotNull
    private BigDecimal custas5;
    @NotNull
    private BigDecimal custas6;
    @NotNull
    private BigDecimal custas7;
    @NotNull
    private BigDecimal custas8;
    @NotNull
    private BigDecimal custas9;
    @NotNull
    private BigDecimal custas10;
    private Long idProtocoloServico;

    public ProtocoloServicoCustasVO(BigDecimal custas1, BigDecimal custas2, BigDecimal custas3, BigDecimal custas4) {
        this.custas1 = custas1;
        this.custas2 = custas2;
        this.custas3 = custas3;
        this.custas4 = custas4;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTabelaCusta() {
        return tabelaCusta;
    }

    public void setTabelaCusta(String tabelaCusta) {
        this.tabelaCusta = tabelaCusta;
    }

    public BigDecimal getVlBase() {
        return vlBase;
    }

    public void setVlBase(BigDecimal vlBase) {
        this.vlBase = vlBase;
    }

    public int getNrQuantidadeBase() {
        return nrQuantidadeBase;
    }

    public void setNrQuantidadeBase(int nrQuantidadeBase) {
        this.nrQuantidadeBase = nrQuantidadeBase;
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

    public Long getIdProtocoloServico() {
        return idProtocoloServico;
    }

    public void setIdProtocoloServico(Long idProtocoloServico) {
        this.idProtocoloServico = idProtocoloServico;
    }
}
