package br.com.exmart.indicadorRTDPJ.service.rest.vo;

import java.math.BigDecimal;

public class ProtocoloPagamentoVO {

    private FormaPagamentoVO formaPagamento;
    private BigDecimal valor;


    public ProtocoloPagamentoVO() {
    }

    public ProtocoloPagamentoVO(FormaPagamentoVO formaPagamento, BigDecimal valor) {
        this.formaPagamento = formaPagamento;
        this.valor = valor;
    }

    public FormaPagamentoVO getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamentoVO formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
