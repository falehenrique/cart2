package br.com.exmart.rtdpjlite.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity()
@Table(name="forma_entrega", schema = "rtdpj")
public class FormaEntrega extends AbstractEntity{
	String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return getNome();
	}
	
}
