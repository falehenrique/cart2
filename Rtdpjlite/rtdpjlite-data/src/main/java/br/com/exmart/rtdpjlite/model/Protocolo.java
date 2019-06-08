package br.com.exmart.rtdpjlite.model;

import br.com.exmart.rtdpjlite.annotations.CpfCnpj;
import br.com.exmart.rtdpjlite.model.custom.JsonObjetoProtocoloUserType;
import br.com.exmart.rtdpjlite.model.custom.JsonServicoCalculadoUserType;
import br.com.exmart.rtdpjlite.model.custom.TipoProtocoloConverter;
import br.com.exmart.rtdpjlite.model.portal.UsuarioPortal;
import br.com.exmart.rtdpjlite.service.CalcularPrazoProtocoloService;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloVO;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import br.com.exmart.util.TemplateChave;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@TypeDefs({@TypeDef(name = "JsonObjetoProtocoloUserType", typeClass = JsonObjetoProtocoloUserType.class),
		@TypeDef(name = "JsonServicoCalculadoUserType", typeClass = JsonServicoCalculadoUserType.class)})
@SuppressWarnings("serial")
@NamedEntityGraphs({
		@NamedEntityGraph( name = "Protocolo.grid", attributeNodes = {
				@NamedAttributeNode(value = "tipo"),
				@NamedAttributeNode(value = "natureza"),
				@NamedAttributeNode(value = "cliente"),
				@NamedAttributeNode(value = "subNatureza"),
				@NamedAttributeNode(value = "statusProtocolo", subgraph = "FetchManagers.Subgraph.Status")
		},
		subgraphs = {
				@NamedSubgraph(name = "FetchManagers.Subgraph.Status", type = StatusProtocolo.class, attributeNodes = {@NamedAttributeNode(value = "status")}),
				@NamedSubgraph(name = "FetchManagers.Subgraph.Status", type = StatusProtocolo.class, attributeNodes = {@NamedAttributeNode(value = "usuario")})
		}),
		@NamedEntityGraph( name = "Protocolo.tudo", attributeNodes = {
				@NamedAttributeNode(value = "tipo"),
				@NamedAttributeNode(value = "natureza"),
				@NamedAttributeNode(value = "cartorioParceiroList"),
				@NamedAttributeNode(value = "formaEntrega"),
				@NamedAttributeNode(value = "partesProtocolo"),
				@NamedAttributeNode(value = "intimacaosProtocolo"),
				@NamedAttributeNode(value = "checklist"),
				@NamedAttributeNode(value = "objetos"),
				@NamedAttributeNode(value = "cliente"),
				@NamedAttributeNode(value = "subNatureza"),
				@NamedAttributeNode(value = "statusProtocolo", subgraph = "FetchManagers.Subgraph.Status")
		},
				subgraphs = {
						@NamedSubgraph(name = "FetchManagers.Subgraph.Status", type = StatusProtocolo.class, attributeNodes = {@NamedAttributeNode(value = "status")}),
						@NamedSubgraph(name = "FetchManagers.Subgraph.Status", type = StatusProtocolo.class, attributeNodes = {@NamedAttributeNode(value = "usuario")})
				})
})
@Table(schema = "rtdpj", name = "protocolo")
@NamedStoredProcedureQuery(name = "Protocolo.reverter", procedureName = "rtdpj.reverter_protocolo", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "numero", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "tipo", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "id", type = Integer.class)})
public class Protocolo extends AbstractEntity{
	protected static Logger logger= LoggerFactory.getLogger(Protocolo.class);
	Long numero;
	@Column(name = "tipo_protocolo_id")
	@Convert(converter = TipoProtocoloConverter.class)
	@NotNull
	private TipoProtocolo tipo;
	@OneToOne
	@NotNull
	private Natureza natureza;
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="protocolo_id")
	private Set<ProtocoloCartorioParceiro> cartorioParceiroList;
	@OneToOne
	private SubNatureza subNatureza;
	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	private FormaEntrega formaEntrega;
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="protocolo_id")
	@JsonIgnore
	Set<ParteProtocolo> partesProtocolo;
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="protocolo_id")
	@NotNull
	private Set<StatusProtocolo> statusProtocolo;
	@JsonIgnore
	@Type(type = "JsonServicoCalculadoUserType")
	List<ServicoCalculado> servicos;
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="protocolo_id")
	@JsonIgnore
	@OrderBy("dia asc")
	Set<IntimacaoProtocolo> intimacaosProtocolo;
	private Integer vias = 0;
	@Column
	@Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
			parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "BRL")})
	@JsonIgnore
	private Money valor;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data_doc;
	private String parte;
	@CpfCnpj
	private String parteDocumento;
	@Column(name = "data_protocolo")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDateTime dataProtocolo = LocalDateTime.now();
	@Column(name = "dt_protocolo_informado_portal")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDateTime dataProtocoloInformadoPortal;
	@Column(name = "dt_registro_informado_portal")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDateTime dataRegistroInformadoPortal;
	@Column(name = "dt_certidao_informado_portal")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDateTime dataCertidaoInformadoPortal;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data_prevista;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data_vencimento;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDateTime data_entrega;
	@Column(name = "pasta_pj")
	private String pastaPJ;
	private String responsavel;
	@NotEmpty
	private String apresentante;
	private String apresentante_rg;
	private String telefone;
	@Email
	private String email;
	@Column(length=10485760)
	private String observacoes;
//	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
//	@JoinColumn(name="protocolo_id")
//	@JsonIgnore
//	private Set<PagamentoProtocolo> pagamentoProtocolo;

	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="protocolo_id")
	@JsonIgnore
	private Set<ChecklistProtocolo> checklist;

	@Type(type = "JsonObjetoProtocoloUserType")
	@JsonIgnore
	private List<ObjetoProtocoloVO> objetos;
	@Column( name = "registro_referencia")
	private String registroReferencia;
	@Column( name = "numero_registro_referencia")
	private Long numeroRegistroReferencia;

	@Column( name = "numero_registro")
	private String numeroRegistro;
	@Column( name = "observacao_registro", length=10485760)
	private String observacaoRegistro;
	@Column( name = "situacao_atual_registro",length=10485760)
	private String situacaoAtualRegistro;
	@Column(name = "dt_registro")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDateTime dtRegistro;
	@Column(name = "dt_devolvido")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDateTime dtDevolvido;

	@Column Long nrExameCalculoReferencia;

	@Column(length=10485760, name = "texto_carimbo")
	@JsonIgnore
	private String textoCarimbo;
	@Column(length=10485760, name = "texto_certidao")
	@JsonIgnore
	private String textoCertidao;
	@JsonIgnore
	@OneToOne
	private Cliente cliente;

	@Column(name = "ic_guarda_conservacao")
	private Boolean icGuardaConservacao = false;
	@Column(name = "ic_possui_sigilo_legal")
	private Boolean icPossuiSigiloLegal = false;
	@Column(name = "pedido_id")
	private Long pedido;
	@Column(name="protocolo_indisponibilidade")
	private String protocoloIndisponibilidade;

	@Column(name="protocolo_cancelamento_indisponibilidade")
	private String protocoloCancelamentoIndisponibilidade;
	@Column(name = "selo_digital_id")
	private Integer seloDigitalId;

	@Transient
	@JsonIgnore
	private InputStream arquivo;
	@Column(name = "ic_registrar_titulo_outra_praca")
	private boolean icRegistrarTituloOutraPraca = false;

	@OneToOne
	@JoinColumn(name =  "responsavel_id")
	private UsuarioPortal responsavelPedido;

	@Column(name = "nr_contrato")
	private String nrContrato;
	@Column(name = "vl_deposito_previo")
	private BigDecimal vlDepositoPrevio;
	
	public Natureza getNatureza() {
		return natureza;
	}
	public void setNatureza(Natureza natureza) {
		this.natureza = natureza;
	}

	@JsonIgnore
	public StatusProtocolo getStatus() {
		StatusProtocolo retorno = null;
		if(this.statusProtocolo == null){
			return null;
		}
		for(StatusProtocolo status : this.statusProtocolo){
			if(!status.getStatus().isIcSistema()) {
				if(retorno == null){
					retorno = status;
				}
				if (status.getData().isAfter(retorno.getData())) {
					retorno = status;
				}
			}
		}
		return retorno;
	}

	public Set<StatusProtocolo> getStatusProtocolo() {
		if(statusProtocolo == null){
			this.statusProtocolo =  new HashSet<>();
		}
		return statusProtocolo;
	}

//	public void setStatusProtocolo(Set<StatusProtocolo> statusProtocolo) {
//		this.statusProtocolo = statusProtocolo;
//	}

	public Integer getVias() {
		return vias;
	}
	public void setVias(Integer vias) {
		this.vias = vias;
	}
	public Money getValor() {
		return valor;
	}
	public void setValor(Money valor) {
		this.valor = valor;
	}
	public LocalDate getDataDoc() {
		return data_doc;
	}
	public void setDataDoc(LocalDate data_doc) {
		this.data_doc = data_doc;
	}
	public LocalDate getDataPrevista() {
		return data_prevista;
	}
	public void setDataPrevista(LocalDate data_prevista) {
		this.data_prevista = data_prevista;
	}
	public LocalDate getDataVencimento() {
		return data_vencimento;
	}
	public void setDataVencimento(LocalDate data_vencimento) {
		this.data_vencimento = data_vencimento;
	}
	public LocalDateTime getDataEntrega() {
		return data_entrega;
	}
	public void setDataEntrega(LocalDateTime data_entrega) {
		this.data_entrega = data_entrega;
	}
	public String getPastaPJ() {
		return pastaPJ;
	}
	public void setPastaPJ(String pastaPJ) {
		this.pastaPJ = pastaPJ;
	}

	public String getApresentante() {
		return apresentante;
	}
	public void setApresentante(String apresentante) {
		this.apresentante = apresentante;
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
	public String getObservacoes() {
		return observacoes;
	}
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	public TipoProtocolo getTipo() {
		return tipo;
	}
	public void setTipo(TipoProtocolo tipo) {
		this.tipo = tipo;
	}

	public LocalDateTime getDataProtocolo() {
		return dataProtocolo;
	}

	public void setDataProtocolo(LocalDateTime dataProtocolo) {
		this.dataProtocolo = dataProtocolo;
	}

	@TemplateChave(nomeChave = "protocolo_numero")
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public LocalDate getData_doc() {
		return data_doc;
	}
	public void setData_doc(LocalDate data_doc) {
		this.data_doc = data_doc;
	}
	public LocalDate getData_prevista() {
		return data_prevista;
	}
	public void setData_prevista(LocalDate data_prevista) {
		this.data_prevista = data_prevista;
	}
	public LocalDate getData_vencimento() {
		return data_vencimento;
	}
	public void setData_vencimento(LocalDate data_vencimento) {
		this.data_vencimento = data_vencimento;
	}
	public LocalDateTime getData_entrega() {
		return data_entrega;
	}
	public void setData_entrega(LocalDateTime data_entrega) {
		this.data_entrega = data_entrega;
	}
	public String getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getApresentanteRg() {
		return apresentante_rg;
	}

	public void setApresentanteRg(String apresentante_rg) {
		this.apresentante_rg = apresentante_rg;
	}

	public Set<ParteProtocolo> getPartesProtocolo() {
		if(this.partesProtocolo == null){
			this.partesProtocolo = new HashSet<>();
		}
		return partesProtocolo;
	}

	public void setPartesProtocolo(Set<ParteProtocolo> partesProtocolo) {
		this.partesProtocolo = partesProtocolo;
	}

	public String getParte() {
		return parte;
	}

	public void setParte(String parte) {
		this.parte = parte;
	}

	public String getParteDocumento() {
		return this.parteDocumento;
	}

	public void setParteDocumento(String parteDocumento) {
		this.parteDocumento = parteDocumento;
	}

	public List<ObjetoProtocoloVO> getObjetos() {
		return objetos;
	}

	public void setObjetos(List<ObjetoProtocoloVO> objetos) {
		this.objetos = objetos;
	}

	public LocalDateTime getData_protocolo() {
		return dataProtocolo;
	}

	public void setData_protocolo(LocalDateTime dataProtocolo) {
		this.dataProtocolo = dataProtocolo;
	}

	public String getApresentante_rg() {
		return apresentante_rg;
	}

	public void setApresentante_rg(String apresentante_rg) {
		this.apresentante_rg = apresentante_rg;
	}

	public InputStream getArquivo() {
		return arquivo;
	}

	public void setArquivo(InputStream arquivo) {
		this.arquivo = arquivo;
	}

	public String getRegistroReferencia() {
		return registroReferencia;
	}

	public void setRegistroReferencia(String registroReferencia) {
		this.registroReferencia = registroReferencia;
	}

	public Set<ChecklistProtocolo> getChecklist() {
		return checklist;
	}

	public void setChecklist(Set<ChecklistProtocolo> checklist) {
		this.checklist = checklist;
	}

	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public String getObservacaoRegistro() {
		return observacaoRegistro;
	}

	public void setObservacaoRegistro(String observacaoRegistro) {
		this.observacaoRegistro = observacaoRegistro;
	}

	public String getSituacaoAtualRegistro() {
		return situacaoAtualRegistro;
	}

	public void setSituacaoAtualRegistro(String situacaoAtualRegistro) {
		this.situacaoAtualRegistro = situacaoAtualRegistro;
	}

	public LocalDateTime getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(LocalDateTime dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	public FormaEntrega getFormaEntrega() {
		return formaEntrega;
	}

	public void setFormaEntrega(FormaEntrega formaEntrega) {
		this.formaEntrega = formaEntrega;
	}


	public Set<IntimacaoProtocolo> getIntimacaosProtocolo() {
		if(intimacaosProtocolo == null){
			this.intimacaosProtocolo =  new HashSet<>();
		}
		return intimacaosProtocolo;
	}

	public void setIntimacaosProtocolo(Set<IntimacaoProtocolo> intimacaosProtocolo) {
		this.intimacaosProtocolo = intimacaosProtocolo;
	}

	public Long getNrExameCalculoReferencia() {
		return nrExameCalculoReferencia;
	}

	public void setNrExameCalculoReferencia(Long nrExameCalculoReferencia) {
		this.nrExameCalculoReferencia = nrExameCalculoReferencia;
	}

	public Protocolo clone(TipoProtocolo tipo, CalcularPrazoProtocoloService calcularPrazoProtocolo) {
		Protocolo retorno = new Protocolo();
		retorno.setTipo(tipo);

		retorno.setNatureza(this.getNatureza());
		retorno.setVias(this.getVias());
		retorno.setValor(this.getValor());
		retorno.setDataDoc(this.getDataDoc());
		retorno.setParteDocumento(this.getParteDocumento());
		retorno.setParte(this.getParte());
		retorno.setRegistroReferencia(this.getRegistroReferencia());
		retorno.setPastaPJ(this.getPastaPJ());
		retorno.setFormaEntrega(this.getFormaEntrega());
		retorno.setSubNatureza(this.getSubNatureza());
		retorno.setCliente(this.getCliente());
		retorno.setResponsavelPedido(this.getResponsavelPedido());
		retorno.setNrContrato(this.getNrContrato());

		if(this.getCartorioParceiroList().size() > 0) {
			for(ProtocoloCartorioParceiro novo : this.getCartorioParceiroList()){
				retorno.getCartorioParceiroList().add(novo.clone());
			}
		}

		if(this.getPartesProtocolo().size() > 0){
		    for(ParteProtocolo parteProtocolo : this.getPartesProtocolo()){
		        retorno.getPartesProtocolo().add(parteProtocolo.clone());
            }
        }

		retorno.setServicos(this.getServicos());


		retorno.setApresentante(this.getApresentante());
		retorno.setResponsavel(this.getResponsavel());
		retorno.setApresentanteRg(this.getApresentanteRg());
		retorno.setTelefone(this.getTelefone());
		retorno.setEmail(this.getEmail());
		retorno.setObservacoes(this.getObservacoes());
		retorno.setProtocoloCancelamentoIndisponibilidade(this.protocoloCancelamentoIndisponibilidade);
		retorno.setProtocoloIndisponibilidade(this.protocoloIndisponibilidade);

		retorno.setDataProtocolo(LocalDateTime.now());

		//TODO deve calcular a data
		LocalDate dtCalculo = LocalDate.now();
		retorno.setDataPrevista(calcularPrazoProtocolo.calcularData(this.getNatureza().getDiasPrevisao(), true, dtCalculo));
		retorno.setDataVencimento(calcularPrazoProtocolo.calcularData(this.getNatureza().getDiasVencimento(), true, dtCalculo));

		return retorno;
	}

    public boolean isRegistrado() {
        for(StatusProtocolo status : this.statusProtocolo){
        	if(status.getStatus().getNome().equalsIgnoreCase("REGISTRADO")){
        		return true;
			}
		}
		return false;
    }

	public String getTextoCarimbo() {
		return textoCarimbo;
	}

	public void setTextoCarimbo(String textoCarimbo) {
		this.textoCarimbo = textoCarimbo;
	}

	public String getTextoCertidao() {
		return textoCertidao;
	}

	public void setTextoCertidao(String textoCertidao) {
		this.textoCertidao = textoCertidao;
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

	public SubNatureza getSubNatureza() {
		return subNatureza;
	}

	public void setSubNatureza(SubNatureza subNatureza) {
		this.subNatureza = subNatureza;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		Protocolo.logger = logger;
	}



	public void setStatusProtocolo(Set<StatusProtocolo> statusProtocolo) {
		this.statusProtocolo = statusProtocolo;
	}

	public List<ServicoCalculado> getServicos() {
	    if(servicos == null)
	        return new ArrayList<>();
		return servicos;
	}

	public void setServicos(List<ServicoCalculado> servicosProtocolo) {
		this.servicos = servicosProtocolo;
	}

	public Long getNumeroRegistroReferencia() {
		return numeroRegistroReferencia;
	}

	public void setNumeroRegistroReferencia(Long numeroRegistroReferencia) {
		this.numeroRegistroReferencia = numeroRegistroReferencia;
	}

	public Long getPedido() {
		return pedido;
	}

	public void setPedido(Long pedido) {
		this.pedido = pedido;
	}

	public LocalDateTime getDataProtocoloInformadoPortal() {
		return dataProtocoloInformadoPortal;
	}

	public void setDataProtocoloInformadoPortal(LocalDateTime dataProtocoloInformadoPortal) {
		this.dataProtocoloInformadoPortal = dataProtocoloInformadoPortal;
	}

	public LocalDateTime getDataRegistroInformadoPortal() {
		return dataRegistroInformadoPortal;
	}

	public void setDataRegistroInformadoPortal(LocalDateTime dataRegistroInformadoPortal) {
		this.dataRegistroInformadoPortal = dataRegistroInformadoPortal;
	}

	public LocalDateTime getDataCertidaoInformadoPortal() {
		return dataCertidaoInformadoPortal;
	}

	public void setDataCertidaoInformadoPortal(LocalDateTime dataCertidaoInformadoPortal) {
		this.dataCertidaoInformadoPortal = dataCertidaoInformadoPortal;
	}

	public LocalDateTime getDtDevolvido() {
		return dtDevolvido;
	}

	public void setDtDevolvido(LocalDateTime dtDevolvido) {
		this.dtDevolvido = dtDevolvido;
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

	public Long getRegistroFinanceiro() {
		if(TipoProtocolo.isCertidao(this.tipo)){
			return this.numero;
		}else {
			Long retorno = Long.getLong(this.numeroRegistro);
			return retorno;
		}
	}

	public Integer getSeloDigitalId() {
		return seloDigitalId;
	}

	public void setSeloDigitalId(Integer seloDigitalId) {
		this.seloDigitalId = seloDigitalId;
	}

	public boolean isIcRegistrarTituloOutraPraca() {
		return icRegistrarTituloOutraPraca;
	}

	public void setIcRegistrarTituloOutraPraca(boolean icRegistrarTituloOutraPraca) {
		this.icRegistrarTituloOutraPraca = icRegistrarTituloOutraPraca;
	}

	public Set<ProtocoloCartorioParceiro> getCartorioParceiroList() {
		if(this.cartorioParceiroList == null)
			this.cartorioParceiroList = new HashSet<>();
		return cartorioParceiroList;
	}

	public void setCartorioParceiroList(Set<ProtocoloCartorioParceiro> cartorioParceiroList) {
		this.cartorioParceiroList = cartorioParceiroList;
	}

	public UsuarioPortal getResponsavelPedido() {
		return responsavelPedido;
	}

	public void setResponsavelPedido(UsuarioPortal responsavelPedido) {
		this.responsavelPedido = responsavelPedido;
	}

	public List<ServicoCalculado> cloneServicos() {
        List<ServicoCalculado> retorno = new ArrayList<ServicoCalculado>(this.servicos);
        for (ServicoCalculado servicoCalculado : retorno) {
            servicoCalculado.setIdServicoCalculado(null);
        }
		return retorno;
	}

	public String getNrContrato() {
		return nrContrato;
	}

	public void setNrContrato(String nrContrato) {
		this.nrContrato = nrContrato;
	}

	public BigDecimal getVlDepositoPrevio() {
		if(this.vlDepositoPrevio == null)
			return BigDecimal.ZERO;
		return vlDepositoPrevio;
	}

	public void setVlDepositoPrevio(BigDecimal vlDepositoPrevio) {
		this.vlDepositoPrevio = vlDepositoPrevio;
	}
}
