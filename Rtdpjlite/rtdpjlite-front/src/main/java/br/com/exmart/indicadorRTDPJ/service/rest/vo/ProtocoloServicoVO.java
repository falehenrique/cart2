package br.com.exmart.indicadorRTDPJ.service.rest.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by fabioebner on 28/05/17.
 */
public class ProtocoloServicoVO {
    private Long id;
    private Long idProtocolo;
    private Long idProtocoloTipo;
    @NotNull
    private Integer paginas =0;
    private String microfilme;
    @NotNull
    private BigDecimal valorBase = BigDecimal.ZERO;
    private Integer divisor;
    private String formaCalculo;

    @NotNull
    private ServicoVO servico;
    List<ProtocoloServicoCustasVO> custas;

    @JsonIgnore
    private BigDecimal custas1;
    @JsonIgnore
    private BigDecimal custas2;
    @JsonIgnore
    private BigDecimal custas3;
    @JsonIgnore
    private BigDecimal custas4;
    @JsonIgnore
    private BigDecimal custas5;
    @JsonIgnore
    private BigDecimal custas6;
    @JsonIgnore
    private BigDecimal custas7;
    @JsonIgnore
    private BigDecimal custas8;
    @JsonIgnore
    private BigDecimal custas9;
    @JsonIgnore
    private BigDecimal custas10;


    public ProtocoloServicoVO(BigDecimal valorBase, String formaCalculo, ServicoVO servico, List<ProtocoloServicoCustasVO> custas) {
        this.valorBase = valorBase;
        this.formaCalculo = formaCalculo;
        this.servico = servico;
        this.custas = custas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProtocolo() {
        return idProtocolo;
    }

    public void setIdProtocolo(Long idProtocolo) {
        this.idProtocolo = idProtocolo;
    }

    public Long getIdProtocoloTipo() {
        return idProtocoloTipo;
    }

    public void setIdProtocoloTipo(Long idProtocoloTipo) {
        this.idProtocoloTipo = idProtocoloTipo;
    }

    public Integer getPaginas() {
        return paginas;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    public String getMicrofilme() {
        return microfilme;
    }

    public void setMicrofilme(String microfilme) {
        this.microfilme = microfilme;
    }

    public BigDecimal getValorBase() {
        return valorBase;
    }

    public void setValorBase(BigDecimal valorBase) {
        this.valorBase = valorBase;
    }

    public Integer getDivisor() {
        return divisor;
    }

    public void setDivisor(Integer divisor) {
        this.divisor = divisor;
    }

    public ServicoVO getServico() {
        return servico;
    }

    public void setServico(ServicoVO servico) {
        this.servico = servico;
    }

    public List<ProtocoloServicoCustasVO> getCustas() {
        return custas;
    }

    public void setCustas(List<ProtocoloServicoCustasVO> custas) {
        this.custas = custas;
    }


    public String getFormaCalculo() {
        return formaCalculo;
    }

    public void setFormaCalculo(String formaCalculo) {
        this.formaCalculo = formaCalculo;
    }

    public BigDecimal getCustas1() {
        BigDecimal retorno = BigDecimal.ZERO;
        for(ProtocoloServicoCustasVO custa: custas){
           retorno= retorno.add(custa.getCustas1());
        }
        return retorno;
    }


    public BigDecimal getCustas2() {
        BigDecimal retorno = BigDecimal.ZERO;
        for(ProtocoloServicoCustasVO custa: custas){
            retorno= retorno.add(custa.getCustas2());
        }
        return retorno;
    }


    public BigDecimal getCustas3() {
        BigDecimal retorno = BigDecimal.ZERO;
        for(ProtocoloServicoCustasVO custa: custas){
            retorno= retorno.add(custa.getCustas3());
        }
        return retorno;
    }



    public BigDecimal getCustas4() {
        BigDecimal retorno = BigDecimal.ZERO;
        for(ProtocoloServicoCustasVO custa: custas){
            retorno= retorno.add(custa.getCustas4());
        }
        return retorno;
    }



    public BigDecimal getCustas5() {
        BigDecimal retorno = BigDecimal.ZERO;
        for(ProtocoloServicoCustasVO custa: custas){
            retorno= retorno.add(custa.getCustas5());
        }
        return retorno;
    }


    public BigDecimal getCustas6() {
        BigDecimal retorno = BigDecimal.ZERO;
        for(ProtocoloServicoCustasVO custa: custas){
            retorno= retorno.add(custa.getCustas6());
        }
        return retorno;
    }


    public BigDecimal getCustas7() {
        BigDecimal retorno = BigDecimal.ZERO;
        for(ProtocoloServicoCustasVO custa: custas){
            retorno= retorno.add(custa.getCustas7());
        }
        return retorno;
    }


    public BigDecimal getCustas8() {
        BigDecimal retorno = BigDecimal.ZERO;
        for(ProtocoloServicoCustasVO custa: custas){
            retorno= retorno.add(custa.getCustas8());
        }
        return retorno;
    }


    public BigDecimal getCustas9() {
        BigDecimal retorno = BigDecimal.ZERO;
        for(ProtocoloServicoCustasVO custa: custas){
            retorno= retorno.add(custa.getCustas9());
        }
        return retorno;
    }


    public BigDecimal getCustas10() {
        BigDecimal retorno = BigDecimal.ZERO;
        for(ProtocoloServicoCustasVO custa: custas){
            retorno= retorno.add(custa.getCustas10());
        }
        return retorno;
    }

    public BigDecimal getTotal(){
        return getCustas1().add(getCustas2().add(getCustas3().add(getCustas4())));
    }

}
