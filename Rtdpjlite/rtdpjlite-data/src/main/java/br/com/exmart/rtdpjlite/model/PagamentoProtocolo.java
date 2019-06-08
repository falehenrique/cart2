package br.com.exmart.rtdpjlite.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

@SuppressWarnings("serial")
@Entity()
@Table(name="pagamento_protocolo", schema = "rtdpj")
public class PagamentoProtocolo extends AbstractEntity {
	private static final CurrencyUnit BRL = CurrencyUnit.of("BRL");
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="forma_pagamento_id")
	private FormaPagamento formaPagamento;
	@Column
	@Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
	    parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "BRL")})

	private Money valor;
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}
	//TODO quando a forma de pagamento for FATURADO, salvar o Cliente
	@Transient
	private Cliente cliente;
	
	//TODO Setar data de pagamento
	@Transient
	private Date data_pagamento;
	
	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	public Money getValor() {
		return valor;
	}
	public void setValor(Money valor) {
		this.valor = valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = Money.of(BRL,valor);
	}
	public void setValor(Float valor) {
		this.valor = Money.of(BRL,valor);
	}
	public void setValor(String valor) {
		this.valor = Money.of(BRL,new BigDecimal(valor));
	}

	@Override
	public String toString() {
		return "PagamentoProtocolo [formaPagamento=" + formaPagamento + ", valor=" + valor + "]";
	}
}
