package br.com.exmart.rtdpjlite.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity()
@Table(name="item_tabela", schema = "rtdpj")
public class ItemTabela extends AbstractEntity{
	private String nome;
	private String descricao;
	private boolean quantidade;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public boolean isQuantidade() {
		return quantidade;
	}
	public void setQuantidade(boolean quantidade) {
		this.quantidade = quantidade;
	}
}
