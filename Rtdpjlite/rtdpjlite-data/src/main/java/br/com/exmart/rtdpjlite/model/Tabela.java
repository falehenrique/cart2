package br.com.exmart.rtdpjlite.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity()
@Table(name = "tabela", schema = "rtdpj")
@SuppressWarnings("serial")
public class Tabela extends AbstractEntity{
	private String nome;
	private Date inicio_vigencia;
	private Date fim_vigencia;

	public Date getInicio_vigencia() {
		return inicio_vigencia;
	}
	public void setInicio_vigencia(Date inicio_vigencia) {
		this.inicio_vigencia = inicio_vigencia;
	}
	public Date getFim_vigencia() {
		return fim_vigencia;
	}
	public void setFim_vigencia(Date fim_vigencia) {
		this.fim_vigencia = fim_vigencia;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
