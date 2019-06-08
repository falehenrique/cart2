package br.com.exmart.rtdpjlite.model;

import br.com.exmart.rtdpjlite.model.custom.TipoEmissaoCertidaoConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity()
@Table(name = "natureza", schema = "rtdpj")
@SuppressWarnings("serial")
//@Cacheable
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@NamedEntityGraphs({@NamedEntityGraph( name = "Natureza.subNaturezas", attributeNodes = @NamedAttributeNode("subNaturezas"))})
public class Natureza extends AbstractEntity{
	String nome;
	@Column(name = "dias_previsao")
	@JsonIgnore
	private int diasPrevisao;
	@Column(name = "dias_vencimento")
	@JsonIgnore
	private int diasVencimento;
	@Column(name = "natureza_tipo_id")
	private Long naturezaTipoId;
	@Column(name = "dias_reentrada")
	@JsonIgnore
	private int diasReentrada;
	@Column(name = "tipo_emissao_certidao_id")
	@Convert(converter = TipoEmissaoCertidaoConverter.class)
	@JsonIgnore
	private TipoEmissaoCertidao tipoEmissaoCertidao;
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="natureza_id")
	@JsonIgnore
	@OrderBy("nome")
//	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	private Set<SubNatureza> subNaturezas;

	@NotNull
	@Column(name = "ic_certidao_digital")
	private boolean icCertidaoDigital;

	@OneToOne
	@NotNull
	private Carimbo carimbo;


	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getDiasPrevisao() {
		return diasPrevisao;
	}

	public void setDiasPrevisao(int diasPrevisao) {
		this.diasPrevisao = diasPrevisao;
	}

	public int getDiasVencimento() {
		return diasVencimento;
	}

	public void setDiasVencimento(int diasVencimento) {
		this.diasVencimento = diasVencimento;
	}

	public int getDiasReentrada() {
		return diasReentrada;
	}

	public void setDiasReentrada(int diasReentrada) {
		this.diasReentrada = diasReentrada;
	}

	public TipoEmissaoCertidao getTipoEmissaoCertidao() {
		return tipoEmissaoCertidao;
	}

	public void setTipoEmissaoCertidao(TipoEmissaoCertidao tipoEmissaoCertidao) {
		this.tipoEmissaoCertidao = tipoEmissaoCertidao;
	}

	@Override
	public String toString() {
		return "Natureza [nome=" + nome + "]";
	}

	public Set<SubNatureza> getSubNaturezas() {
		return subNaturezas;
	}

	public void setSubNaturezas(Set<SubNatureza> subNaturezas) {
		this.subNaturezas = subNaturezas;
	}

	public Long getNaturezaTipoId() {
		return naturezaTipoId;
	}

	public void setNaturezaTipoId(Long naturezaTipoId) {
		this.naturezaTipoId = naturezaTipoId;
	}

	public Carimbo getCarimbo() {
		return carimbo;
	}


	public boolean isIcCertidaoDigital() {
		return icCertidaoDigital;
	}

	public void setIcCertidaoDigital(boolean icCertidaoDigital) {
		this.icCertidaoDigital = icCertidaoDigital;
	}

	public void setCarimbo(Carimbo carimbo) {
		this.carimbo = carimbo;
	}
}
