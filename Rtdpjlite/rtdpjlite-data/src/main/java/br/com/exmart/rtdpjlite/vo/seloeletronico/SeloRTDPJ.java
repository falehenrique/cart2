package br.com.exmart.rtdpjlite.vo.seloeletronico;

import br.com.exmart.rtdpjlite.model.ParteProtocolo;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SeloRTDPJ {
    private String especialidade;
    private String tipo;
    private String nrCnsCartorio;
    private Boolean icGuardaConservacao;
    private String natureza;
    private String subNatureza;
    private Long registro;
    private List<SeloParteRTDPJ> partes = new ArrayList<>();
    private BigDecimal emolumentos;
    private BigDecimal estado;
    private BigDecimal ipesp;
    private BigDecimal santaCasa;
    private BigDecimal registroCivil;
    private BigDecimal tjsp;
    private BigDecimal iss;
    private BigDecimal mp;
    private BigDecimal total;
    private List<AtoTabelaFormaCalculo> servicos = new ArrayList<>();
    @JsonFormat(pattern = "dd/MM/yyyy H:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime data;
    private Integer idSeloAnterior;
    private Long protocolo;

    public SeloRTDPJ(Protocolo protocolo, Long registro, String cnsCartorio, LocalDateTime data, List<ServicoCalculado> servicosProtocolo, List<SeloParteRTDPJ> partes) {
        this.especialidade = TipoProtocolo.recuperaEspecialidade(protocolo.getTipo());
        if(TipoProtocolo.isCertidao(protocolo.getTipo())) {
            this.tipo = "CERTIDAO";
        }else {
            if(protocolo.getNumeroRegistroReferencia() != null){
                tipo = "AVERBACAO";
            }else{
                tipo = "REGISTRO";
            }
        }
        this.nrCnsCartorio = cnsCartorio;
        this.protocolo = protocolo.getNumero();
        this.icGuardaConservacao = protocolo.getIcGuardaConservacao();
        this.natureza = protocolo.getNatureza().getNome();
        if(protocolo.getSubNatureza() != null){
            this.subNatureza = protocolo.getSubNatureza().getNome();
        }
        this.registro = registro;
        this.partes = partes;

        BigDecimal custas1 = BigDecimal.ZERO;
        BigDecimal custas2 = BigDecimal.ZERO;
        BigDecimal custas3 = BigDecimal.ZERO;
        BigDecimal custas4 = BigDecimal.ZERO;
        BigDecimal custas5 = BigDecimal.ZERO;
        BigDecimal custas6 = BigDecimal.ZERO;
        BigDecimal custas7 = BigDecimal.ZERO;
        BigDecimal custas8 = BigDecimal.ZERO;
        BigDecimal custas9 = BigDecimal.ZERO;

        for(ServicoCalculado servicoCalculado : servicosProtocolo){
            servicos.add(new AtoTabelaFormaCalculo(servicoCalculado.getNmFormaCalculo(), servicoCalculado.getTabelaCusta()));
            try {
                custas1 = custas1.add(servicoCalculado.getCustas1());
                custas2 = custas2.add(servicoCalculado.getCustas2());
                custas3 = custas3.add(servicoCalculado.getCustas3());
                custas4 = custas4.add(servicoCalculado.getCustas4());
                custas5 = custas5.add(servicoCalculado.getCustas5());
                custas6 = custas6.add(servicoCalculado.getCustas6());
                custas7 = custas7.add(servicoCalculado.getCustas7());
                custas8 = custas8.add(servicoCalculado.getCustas8());
                custas9 = custas9.add(servicoCalculado.getCustas9());
            }catch (Exception e){
                //silienciator
            }

        }
        this.emolumentos = custas1;
        this.estado = custas2;
        this.ipesp = custas3;
        this.santaCasa = BigDecimal.ZERO;
        this.registroCivil = custas4;
        this.tjsp = custas5;
        this.mp = custas6;
        this.iss = custas7;
        this.total = emolumentos.add(estado.add(ipesp.add(santaCasa.add(registroCivil.add(tjsp.add(mp.add(iss)))))));

        this.data = data;

        this.idSeloAnterior = protocolo.getSeloDigitalId();

    }

    public SeloRTDPJ() {
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public Boolean getIcGuardaConservacao() {
        return icGuardaConservacao;
    }

    public void setIcGuardaConservacao(Boolean icGuardaConservacao) {
        this.icGuardaConservacao = icGuardaConservacao;
    }

    public Long getRegistro() {
        return registro;
    }

    public void setRegistro(Long registro) {
        this.registro = registro;
    }

    public List<SeloParteRTDPJ> getPartes() {
        return partes;
    }

    public void setPartes(List<SeloParteRTDPJ> partes) {
        this.partes = partes;
    }

    public BigDecimal getEmolumentos() {
        return emolumentos;
    }

    public void setEmolumentos(BigDecimal emolumentos) {
        this.emolumentos = emolumentos;
    }

    public BigDecimal getEstado() {
        return estado;
    }

    public void setEstado(BigDecimal estado) {
        this.estado = estado;
    }

    public BigDecimal getIpesp() {
        return ipesp;
    }

    public void setIpesp(BigDecimal ipesp) {
        this.ipesp = ipesp;
    }

    public BigDecimal getSantaCasa() {
        return santaCasa;
    }

    public void setSantaCasa(BigDecimal santaCasa) {
        this.santaCasa = santaCasa;
    }

    public BigDecimal getRegistroCivil() {
        return registroCivil;
    }

    public void setRegistroCivil(BigDecimal registroCivil) {
        this.registroCivil = registroCivil;
    }

    public BigDecimal getTjsp() {
        return tjsp;
    }

    public void setTjsp(BigDecimal tjsp) {
        this.tjsp = tjsp;
    }

    public BigDecimal getIss() {
        return iss;
    }

    public void setIss(BigDecimal iss) {
        this.iss = iss;
    }

    public BigDecimal getMp() {
        return mp;
    }

    public void setMp(BigDecimal mp) {
        this.mp = mp;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<AtoTabelaFormaCalculo> getServicos() {
        return servicos;
    }

    public void setServicos(List<AtoTabelaFormaCalculo> servicos) {
        this.servicos = servicos;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public String getSubNatureza() {
        return subNatureza;
    }

    public void setSubNatureza(String subNatureza) {
        this.subNatureza = subNatureza;
    }

    public String getNrCnsCartorio() {
        return nrCnsCartorio;
    }

    public void setNrCnsCartorio(String nrCnsCartorio) {
        this.nrCnsCartorio = nrCnsCartorio;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Long getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Long protocolo) {
        this.protocolo = protocolo;
    }

    public Integer getIdSeloAnterior() {
        return idSeloAnterior;
    }

    public void setIdSeloAnterior(Integer idSeloAnterior) {
        this.idSeloAnterior = idSeloAnterior;
    }
}
