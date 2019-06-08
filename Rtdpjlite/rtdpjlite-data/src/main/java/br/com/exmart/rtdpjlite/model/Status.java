package br.com.exmart.rtdpjlite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity()
@Table(name = "status", schema = "rtdpj")
@SuppressWarnings("serial")
public class Status extends AbstractEntity {
	@NotEmpty
	private String nome;
	@NotNull
	@JsonIgnore
	private boolean automatico;
	@NotNull
	@JsonIgnore
	@Column(name =  "bloquear_protocolo")
	private boolean bloquearProtocolo;
	@NotNull
	@Column(name =  "ic_sistema")
	private boolean icSistema;


	public Status() {
		this.icSistema = false;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isAutomatico() {
		return automatico;
	}

	public void setAutomatico(boolean automatico) {
		this.automatico = automatico;
	}

	public boolean isBloquearProtocolo() {
		return bloquearProtocolo;
	}

	public void setBloquearProtocolo(boolean bloquearProtocolo) {
		this.bloquearProtocolo = bloquearProtocolo;
	}

	public boolean isIcSistema() {
		return icSistema;
	}

	public void setIcSistema(boolean icSistema) {
		this.icSistema = icSistema;
	}

	@Override
	public String toString() {
		return "Status [nome=" + nome + "]";
	}
}
