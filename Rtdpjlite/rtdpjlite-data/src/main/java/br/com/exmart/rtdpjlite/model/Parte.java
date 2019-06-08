package br.com.exmart.rtdpjlite.model;

import br.com.exmart.rtdpjlite.annotations.CpfCnpj;
import com.google.common.base.Strings;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.time.LocalDate;

@Entity()
@Table(name = "parte", schema = "rtdpj")
@SuppressWarnings("serial")
@NamedEntityGraphs({
		@NamedEntityGraph( name = "Parte.tudo", attributeNodes = {
				@NamedAttributeNode(value = "tipoDocumento"),
				@NamedAttributeNode(value = "tipoLogradouro"),
				@NamedAttributeNode(value = "nacionalidade"),
				@NamedAttributeNode(value = "estadoCivil"),
				@NamedAttributeNode(value = "profissao"),
				@NamedAttributeNode(value = "regimeBens"),
				@NamedAttributeNode(value = "estado"),
				@NamedAttributeNode(value = "cidade")
		})
})
public class Parte extends AbstractEntity {

	@NotEmpty
	private String nome;
//	@NotEmpty
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

	public String getUuidConjuge() {
		return uuidConjuge;
	}

	public void setUuidConjuge(String uuidConjuge) {
		this.uuidConjuge = uuidConjuge;
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

	public boolean isEqualsBanco(Parte parteEncontradaBanco) {
		if(parteEncontradaBanco == null){
			return false;
		}
		if(!Strings.isNullOrEmpty(this.getNome())) {
			if (!this.getNome().equalsIgnoreCase(parteEncontradaBanco.getNome())) {
				return false;
			}
		}
		if(!Strings.isNullOrEmpty(this.getCpfCnpj())) {
			if (!this.getCpfCnpj().equalsIgnoreCase(parteEncontradaBanco.getCpfCnpj())) {
				return false;
			}
		}
		if(!Strings.isNullOrEmpty(this.getTelefone())) {
			if (!this.getTelefone().equalsIgnoreCase(parteEncontradaBanco.getTelefone())) {
				return false;
			}
		}
		if(!Strings.isNullOrEmpty(this.getNrDocumento())) {
			if (!this.getNrDocumento().equalsIgnoreCase(parteEncontradaBanco.getNrDocumento())) {
				return false;
			}
		}
		if(!Strings.isNullOrEmpty(this.getNmOrgaoExpedidorDocumento())) {
			if (!this.getNmOrgaoExpedidorDocumento().equalsIgnoreCase(parteEncontradaBanco.getNmOrgaoExpedidorDocumento())) {
				return false;
			}
		}
		if(!Strings.isNullOrEmpty(this.getEmail())) {
			if (!this.getEmail().equalsIgnoreCase(parteEncontradaBanco.getEmail())) {
				return false;
			}
		}
		if(!Strings.isNullOrEmpty(this.getCep())) {
			if (!this.getCep().equalsIgnoreCase(parteEncontradaBanco.getCep())) {
				return false;
			}
		}
		if(!Strings.isNullOrEmpty(this.getEndereco())) {
			if (!this.getEndereco().equalsIgnoreCase(parteEncontradaBanco.getEndereco())) {
				return false;
			}
		}
		if(!Strings.isNullOrEmpty(this.getNumero())) {
			if (!this.getNumero().equalsIgnoreCase(parteEncontradaBanco.getNumero())) {
				return false;
			}
		}
		if(!Strings.isNullOrEmpty(this.getComplemento())) {
			if (!this.getComplemento().equalsIgnoreCase(parteEncontradaBanco.getComplemento())) {
				return false;
			}
		}
		if(!Strings.isNullOrEmpty(this.getBairro())) {
			if (!this.getBairro().equalsIgnoreCase(parteEncontradaBanco.getBairro())) {
				return false;
			}
		}


		try{
			if(!this.getNacionalidade().equals(parteEncontradaBanco.getNacionalidade())){
				return false;
			}
		}catch (Exception e){

		}
		try{
			if(!this.getEstadoCivil().equals(parteEncontradaBanco.getEstadoCivil())){
				return false;
			}
		}catch (Exception e){

		}
		try{
			if(!this.getProfissao().equals(parteEncontradaBanco.getProfissao())){
				return false;
			}
		}catch (Exception e){

		}
		try{
			if(!this.getRegimeBens().equals(parteEncontradaBanco.getRegimeBens())){
				return false;
			}
		}catch (Exception e){

		}
		try{
			if(!this.getCidade().equals(parteEncontradaBanco.getCidade())){
				return false;
			}
		}catch (Exception e){

		}

		return true;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public TipoLogradouro getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}
}
