package br.com.exmart.rtdpjlite.interfaces;

import br.com.exmart.rtdpjlite.model.*;

import java.time.LocalDate;

public interface ParteInterface {

    public String getNome();

    public String getCpfCnpj();

    public TipoDocumento getTipoDocumento();

    public String getNrDocumento();

    public String getNmOrgaoExpedidorDocumento();

    public LocalDate getDtEmissaoDocumento();

    public String getTelefone();

    public String getEmail();

    public String getCep();

    public TipoLogradouro getTipoLogradouro();

    public String getEndereco();

    public String getNumero();

    public String getComplemento();

    public String getBairro();

    public String getUuidConjuge();

    public Nacionalidade getNacionalidade();

    public EstadoCivil getEstadoCivil();

    public Profissao getProfissao();

    public RegimeBens getRegimeBens();

    public Estado getEstado();

    public Cidade getCidade();

    public Qualidade getQualidade();

    public Double getVlParticipacao();

    public Integer getQtdCotas();
}
