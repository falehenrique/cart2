package br.com.exmart.rtdpjlite.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity()
@Table(name="forma_calculo", schema = "rtdpj")
@SuppressWarnings("serial")
public class FormaCalculo extends AbstractEntity {
	String nome;
	String formula;

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

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

}
