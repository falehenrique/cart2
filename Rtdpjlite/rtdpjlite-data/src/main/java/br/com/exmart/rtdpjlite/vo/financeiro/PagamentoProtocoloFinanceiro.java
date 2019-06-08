package br.com.exmart.rtdpjlite.vo.financeiro;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentoProtocoloFinanceiro {
    private Long idFormaPagamento;
    private String formaPagamento;
    private BigDecimal valor;
    private LocalDateTime data;



    public Long getIdFormaPagamento() {
        return idFormaPagamento;
    }

    public void setIdFormaPagamento(Long idFormaPagamento) {
        this.idFormaPagamento = idFormaPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
