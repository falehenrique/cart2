package br.com.exmart.rtdpjlite.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.money.Money;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.exmart.rtdpjlite.model.custom.JsonObjetoProtocoloUserType;
import br.com.exmart.rtdpjlite.model.custom.JsonServicoCalculadoUserType;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloVO;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;

@Entity()
@Table(name = "registro", schema = "rtdpj")
@TypeDefs({ @TypeDef(name = "JsonObjetoProtocoloUserType", typeClass = JsonObjetoProtocoloUserType.class),
		@TypeDef(name = "JsonServicoCalculadoUserType", typeClass = JsonServicoCalculadoUserType.class) })
@NamedStoredProcedureQuery(name = "Registro.reverter", procedureName = "rtdpj.reverter_registro", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "registro", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "especialidade", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "id", type = Integer.class) })
public class Registro extends AbstractEntity {
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "registro_id")
	@JsonIgnore
	private Set<IndicadorPessoal> indicadorPessoal;

	@Column(name = "registro")
	@NotEmpty
	private String registro;

	@Column(name = "numero_registro")
	@NotNull
	private Long numeroRegistro;
	@Column(name = "data_registro")
	@NotNull
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDateTime dataRegistro;
	
	@Column(name = "registro_referencia")
	private String registroReferencia;
	@Column(name = "numero_registro_referencia")
	private Long numeroRegistroReferencia;
	@Column(name = "nr_pasta_pj")
	private String nrPastaPj;
	@Column(name = "protocolo_id")
	private Long protocolo;
	@Column(name = "tp_especialidade")
	@NotEmpty
	private String especialidade;
	@Type(type = "JsonObjetoProtocoloUserType")
	@JsonIgnore
	private List<ObjetoProtocoloVO> objetos;
	@JsonIgnore
	@Type(type = "JsonServicoCalculadoUserType")
	List<ServicoCalculado> servicos;
	@Column(name = "observacao", length = 10485760)
	private String observacao;
	@Column(name = "situacao_atual", length = 10485760)
	private String situacaoAtual;
	@OneToOne
	@NotNull
//    @Column(name="natureza_id")
	private Natureza natureza;
	@OneToOne
	@NotNull
//    @Column(name="sub_natureza_id")
	private SubNatureza subNatureza;
	@OneToOne
	@JsonIgnore
	private Cliente cliente;
	@Column(name = "ic_guarda_conservacao")
	private Boolean icGuardaConservacao = false;
	@Column(name = "ic_possui_sigilo_legal")
	private Boolean icPossuiSigiloLegal = false;
	@Column(name = "protocolo_indisponibilidade")
	private String protocoloIndisponibilidade;

	@Column(name = "nr_contrato")
	private String nrContrato;

	@Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
	  	  parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "BRL")})
	@Column(name = "valor")
	private Money valor;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@Column(name = "data_doc")
	private Date dataContrato;

	@Column(name = "protocolo_cancelamento_indisponibilidade")
	private String protocoloCancelamentoIndisponibilidade;

	public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public Long getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(Long numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public LocalDateTime getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(LocalDateTime dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public String getRegistroReferencia() {
		return registroReferencia;
	}

	public void setRegistroReferencia(String registroReferencia) {
		this.registroReferencia = registroReferencia;
	}

	public String getNrPastaPj() {
		return nrPastaPj;
	}

	public void setNrPastaPj(String nrPastaPj) {
		this.nrPastaPj = nrPastaPj;
	}

	public Long getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(Long protocolo) {
		this.protocolo = protocolo;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public List<ObjetoProtocoloVO> getObjetos() {
		return objetos;
	}

	public void setObjetos(List<ObjetoProtocoloVO> objetos) {
		this.objetos = objetos;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getSituacaoAtual() {
		return situacaoAtual;
	}

	public void setSituacaoAtual(String situacaoAtual) {
		this.situacaoAtual = situacaoAtual;
	}

	public Set<IndicadorPessoal> getIndicadorPessoal() {
		if (indicadorPessoal == null)
			this.indicadorPessoal = new HashSet<>();
		return indicadorPessoal;
	}

	public void setIndicadorPessoal(Set<IndicadorPessoal> indicadorPessoal) {
		this.indicadorPessoal = indicadorPessoal;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Boolean getIcGuardaConservacao() {
		return icGuardaConservacao;
	}

	public void setIcGuardaConservacao(Boolean icGuardaConservacao) {
		this.icGuardaConservacao = icGuardaConservacao;
	}

	public Boolean getIcPossuiSigiloLegal() {
		return icPossuiSigiloLegal;
	}

	public void setIcPossuiSigiloLegal(Boolean icPossuiSigiloLegal) {
		this.icPossuiSigiloLegal = icPossuiSigiloLegal;
	}

	public Natureza getNatureza() {
		return natureza;
	}

	public void setNatureza(Natureza natureza) {
		this.natureza = natureza;
	}

	public SubNatureza getSubNatureza() {
		return subNatureza;
	}

	public void setSubNatureza(SubNatureza subNatureza) {
		this.subNatureza = subNatureza;
	}

	public Long getNumeroRegistroReferencia() {
		return numeroRegistroReferencia;
	}

	public void setNumeroRegistroReferencia(Long numeroRegistroReferencia) {
		this.numeroRegistroReferencia = numeroRegistroReferencia;
	}

	public List<ServicoCalculado> getServicos() {
		if (servicos == null)
			servicos = new ArrayList<>();
		return servicos;
	}

	public void setServicos(List<ServicoCalculado> servicos) {
		this.servicos = servicos;
	}

	public String getProtocoloIndisponibilidade() {
		return protocoloIndisponibilidade;
	}

	public void setProtocoloIndisponibilidade(String protocoloIndisponibilidade) {
		this.protocoloIndisponibilidade = protocoloIndisponibilidade;
	}

	public String getProtocoloCancelamentoIndisponibilidade() {
		return protocoloCancelamentoIndisponibilidade;
	}

	public void setProtocoloCancelamentoIndisponibilidade(String protocoloCancelamentoIndisponibilidade) {
		this.protocoloCancelamentoIndisponibilidade = protocoloCancelamentoIndisponibilidade;
	}

	
	public String getNrContrato() {
		return nrContrato;
	}

	public void setNrContrato(String nrContrato) {
		this.nrContrato = nrContrato;
	}

	public Date getDataContrato() {
		return dataContrato;
	}

	public void setDataContrato(Date dataContrato) {
		this.dataContrato = dataContrato;
	}

	public Money getValor() {
		return valor;
	}

	public void setValor(Money valor) {
		this.valor = valor;
	}

	

}
