package br.com.exmart.rtdpjlite.model;

import br.com.exmart.rtdpjlite.annotations.CpfCnpj;
import br.com.exmart.rtdpjlite.interfaces.ParteInterface;
import br.com.exmart.rtdpjlite.service.rest.fonetica.FoneticaService;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity()
@Table(name = "indicador_pessoal", schema = "rtdpj")
public class IndicadorPessoal extends AbstractEntity implements ParteInterface {
    @OneToOne(fetch= FetchType.EAGER)
    private Registro registro;
    @NotEmpty
    private String nome;
    private String nomeFonetico;
//    @NotEmpty
    @CpfCnpj
    private String cpfCnpj;
    @OneToOne(fetch= FetchType.EAGER)
    private TipoDocumento tipoDocumento;
    private String nrDocumento;
    private String nmOrgaoExpedidorDocumento;
    private LocalDate dtEmissaoDocumento;

    private String telefone;
    @Email
    private String email;
    private String cep;
    @OneToOne(fetch= FetchType.EAGER)
    private TipoLogradouro tipoLogradouro;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    @Column(name = "uuid_conjuge")
    private String uuidConjuge;


    @OneToOne(fetch= FetchType.EAGER)
    private Nacionalidade nacionalidade;
    @OneToOne(fetch= FetchType.EAGER)
    private EstadoCivil estadoCivil;
    @OneToOne(fetch= FetchType.EAGER)
    private Profissao profissao;
    @OneToOne(fetch= FetchType.EAGER)
    private RegimeBens regimeBens;
    @OneToOne(fetch= FetchType.EAGER)
    private Estado estado;
    @OneToOne(fetch= FetchType.EAGER)
    private Cidade cidade;
    @OneToOne(fetch= FetchType.EAGER)
    @NotNull
    private Qualidade qualidade;
    @Column(name = "vl_participacao")
    private Double vlParticipacao = Double.valueOf(0);

    @Column(name = "qtd_cotas")
    private Integer qtdCotas = 0;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNrDocumento() {
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public String getNmOrgaoExpedidorDocumento() {
        return nmOrgaoExpedidorDocumento;
    }

    public void setNmOrgaoExpedidorDocumento(String nmOrgaoExpedidorDocumento) {
        this.nmOrgaoExpedidorDocumento = nmOrgaoExpedidorDocumento;
    }

    public LocalDate getDtEmissaoDocumento() {
        return dtEmissaoDocumento;
    }

    public void setDtEmissaoDocumento(LocalDate dtEmissaoDocumento) {
        this.dtEmissaoDocumento = dtEmissaoDocumento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public TipoLogradouro getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getUuidConjuge() {
        return uuidConjuge;
    }

    public void setUuidConjuge(String uuidConjuge) {
        this.uuidConjuge = uuidConjuge;
    }

    public Nacionalidade getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(Nacionalidade nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Profissao getProfissao() {
        return profissao;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }

    public RegimeBens getRegimeBens() {
        return regimeBens;
    }

    public void setRegimeBens(RegimeBens regimeBens) {
        this.regimeBens = regimeBens;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
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

    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public String getNomeFonetico() {
        return nomeFonetico;
    }

    public void setNomeFonetico(String nomeFonetico) {
        this.nomeFonetico = nomeFonetico;
    }

    public Parte toParte(){
        Parte retorno = new Parte();
        retorno.setCpfCnpj(this.getCpfCnpj());
        retorno.setNrDocumento(this.getNrDocumento());
        retorno.setNome(this.getNome());
        retorno.setBairro(this.getBairro());
        retorno.setCep(this.getCep());
        retorno.setCidade(this.getCidade());
        retorno.setComplemento(this.getComplemento());
        retorno.setDtEmissaoDocumento(this.getDtEmissaoDocumento());
        retorno.setEmail(this.getEmail());
        retorno.setEndereco(this.getEndereco());
        retorno.setEstado(this.getEstado());
        retorno.setEstadoCivil(this.getEstadoCivil());
        retorno.setNacionalidade(this.getNacionalidade());
        retorno.setNmOrgaoExpedidorDocumento(this.getNmOrgaoExpedidorDocumento());
        retorno.setProfissao(this.getProfissao());
        retorno.setNumero(this.getNumero());
        retorno.setRegimeBens(this.getRegimeBens());
        retorno.setTelefone(this.getTelefone());
        retorno.setTipoDocumento(this.getTipoDocumento());
        retorno.setTipoLogradouro(this.getTipoLogradouro());

        return retorno;
    }

    public static IndicadorPessoal converterPartes(ParteProtocolo parte, Registro registro) {

            IndicadorPessoal novoIndicador = new IndicadorPessoal();
            novoIndicador.setVlParticipacao(parte.getVlParticipacao());
            novoIndicador.setQualidade(parte.getQualidade());
            novoIndicador.setQtdCotas(parte.getQtdCotas());
            novoIndicador.setBairro(parte.getParte().getBairro());
            novoIndicador.setCep(parte.getParte().getCep());
            novoIndicador.setCidade(parte.getParte().getCidade());
            novoIndicador.setComplemento(parte.getParte().getComplemento());
            novoIndicador.setCpfCnpj(parte.getParte().getCpfCnpj());
            novoIndicador.setDtEmissaoDocumento(parte.getParte().getDtEmissaoDocumento());
            novoIndicador.setEmail(parte.getParte().getEmail());
            novoIndicador.setEndereco(parte.getParte().getEndereco());
            novoIndicador.setEstado(parte.getParte().getEstado());
            novoIndicador.setEstadoCivil(parte.getParte().getEstadoCivil());
            novoIndicador.setNacionalidade(parte.getParte().getNacionalidade());
            novoIndicador.setNmOrgaoExpedidorDocumento(parte.getParte().getNmOrgaoExpedidorDocumento());
            novoIndicador.setNome(parte.getParte().getNome());
            novoIndicador.setNrDocumento(parte.getParte().getNrDocumento());
            novoIndicador.setNumero(parte.getParte().getNumero());
            novoIndicador.setProfissao(parte.getParte().getProfissao());
            novoIndicador.setRegimeBens(parte.getParte().getRegimeBens());
            novoIndicador.setTelefone(parte.getParte().getTelefone());
            novoIndicador.setTipoDocumento(parte.getParte().getTipoDocumento());
            novoIndicador.setTipoLogradouro(parte.getParte().getTipoLogradouro());
            novoIndicador.setUuidConjuge(parte.getParte().getUuidConjuge());


            novoIndicador.setRegistro(registro);



        return novoIndicador;
    }


}

