package br.com.exmart.rtdpjlite.model;

import br.com.exmart.rtdpjlite.interfaces.ParteInterface;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity()
@Table(name = "parte_protocolo", schema = "rtdpj")
public class ParteProtocolo extends  AbstractEntity implements ParteInterface {

    @OneToOne(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
    private Parte parte = new Parte();
    @OneToOne(fetch= FetchType.EAGER)
    @NotNull
    private Qualidade qualidade;
    @Column(name = "vl_participacao")
    private Double vlParticipacao = Double.valueOf(0);

    @Column(name = "qtd_cotas")
    private Integer qtdCotas = 0;

    @Transient
    private boolean icIndisponivel = false;

    public ParteProtocolo(Parte parte) {
        this.parte = parte;
    }
    public ParteProtocolo(Parte parte, Qualidade qualidade) {
        this.parte = parte;
        this.qualidade = qualidade;
    }

    public ParteProtocolo() {
    }

    public Parte getParte() {
        return parte;
    }

    public void setParte(Parte parte) {
        this.parte = parte;
    }

    public Qualidade getQualidade() {
        return qualidade;
    }

    public void setQualidade(Qualidade qualidade) {
        this.qualidade = qualidade;
    }


    public Double getVlParticipacao() {
        return vlParticipacao;
    }

    public void setVlParticipacao(Double vlParticipacao) {
        this.vlParticipacao = vlParticipacao;
    }

    public Integer getQtdCotas() {
        return qtdCotas;
    }

    public void setQtdCotas(Integer qtdCotas) {
        this.qtdCotas = qtdCotas;
    }




    public String getNome() {
        return parte.getNome();
    }

    public String getCpfCnpj() {
        return parte.getCpfCnpj();
    }

    public TipoDocumento getTipoDocumento() {
        return parte.getTipoDocumento();
    }

    public String getNrDocumento() {
        return parte.getNrDocumento();
    }

    public String getNmOrgaoExpedidorDocumento() {
        return parte.getNmOrgaoExpedidorDocumento();
    }

    public LocalDate getDtEmissaoDocumento() {
        return parte.getDtEmissaoDocumento();
    }

    public String getTelefone() {
        return parte.getTelefone();
    }

    public String getEmail() {
        return parte.getEmail();
    }

    public String getCep() {
        return parte.getCep();
    }

    public TipoLogradouro getTipoLogradouro() {
        return parte.getTipoLogradouro();
    }

    public String getEndereco() {
        return parte.getEndereco();
    }

    public String getNumero() {
        return parte.getNumero();
    }

    public String getComplemento() {
        return parte.getComplemento();
    }

    public String getBairro() {
        return parte.getBairro();
    }

    public String getUuidConjuge() {
        return parte.getUuidConjuge();
    }

    public Nacionalidade getNacionalidade() {
        return parte.getNacionalidade();
    }

    public EstadoCivil getEstadoCivil() {
        return parte.getEstadoCivil();
    }

    public Profissao getProfissao() {
        return parte.getProfissao();
    }

    public RegimeBens getRegimeBens() {
        return parte.getRegimeBens();
    }

    public Estado getEstado() {
        return parte.getEstado();
    }

    public Cidade getCidade() {
        return parte.getCidade();
    }



    public static ParteProtocolo fromIndicadorPessoal(Parte parte, Qualidade qualidade, Double vlParticipacao, Integer qtdCotas){
        ParteProtocolo retorno = new ParteProtocolo();
        retorno.setParte(parte);
        retorno.setQualidade(qualidade);
        retorno.setVlParticipacao(vlParticipacao);
        retorno.setQtdCotas(qtdCotas);

        return retorno;
    }

    public ParteProtocolo clone(){
        ParteProtocolo retorno = new ParteProtocolo();
        retorno.setQualidade(this.getQualidade());
        retorno.setVlParticipacao(this.getVlParticipacao());
        retorno.setQtdCotas(this.getQtdCotas());
        retorno.setIcIndisponivel(this.isIcIndisponivel());
        retorno.setParte(this.getParte());
        return retorno;
    }

    public boolean isIcIndisponivel() {
        return icIndisponivel;
    }

    public void setIcIndisponivel(boolean icIndisponivel) {
        this.icIndisponivel = icIndisponivel;
    }
}
