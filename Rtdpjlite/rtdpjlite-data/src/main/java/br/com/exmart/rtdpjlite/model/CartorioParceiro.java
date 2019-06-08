package br.com.exmart.rtdpjlite.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cartorio_parceiro", schema = "rtdpj")
public class CartorioParceiro extends  AbstractEntity{
    @NotEmpty
    private String nome;
    @NotEmpty
    private String alias;
    @Email
    private String email;
    private boolean icEnvioEletronico = false;
    @OneToOne
    @NotNull
    private Cidade cidade;
    @OneToOne
    @NotNull
    private Estado estado;
    @Column(name = "qtd_certidao_fisica")
    private Integer qtdCertidaoFisica = 0;
    @CNPJ
    @NotEmpty
    private String cnpj;
    @NotEmpty
    private String oficial;
    @CPF
    @NotEmpty
    private String oficialCpf;
    @NotEmpty
    private String cns;
    @NotEmpty
    private String comarca;



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIcEnvioEletronico() {
        return icEnvioEletronico;
    }

    public void setIcEnvioEletronico(boolean icEnvioEletronico) {
        this.icEnvioEletronico = icEnvioEletronico;
    }


    public Integer getQtdCertidaoFisica() {
        return qtdCertidaoFisica;
    }

    public void setQtdCertidaoFisica(Integer qtdCertidaoFisica) {
        this.qtdCertidaoFisica = qtdCertidaoFisica;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getOficial() {
        return oficial;
    }

    public void setOficial(String oficial) {
        this.oficial = oficial;
    }

    public String getOficialCpf() {
        return oficialCpf;
    }

    public void setOficialCpf(String oficialCpf) {
        this.oficialCpf = oficialCpf;
    }

    public String getCns() {
        return cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }

    public String getComarca() {
        return comarca;
    }

    public void setComarca(String comarca) {
        this.comarca = comarca;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }


    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return this.nome;
    }


}
