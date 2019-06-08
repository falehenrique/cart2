package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.ModalRecebimentos;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.FinanceiroService;
import br.com.exmart.rtdpjlite.service.FormaPagamentoService;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.vo.financeiro.PagamentoProtocoloFinanceiro;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import br.com.exmart.util.BeanLocator;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.HtmlRenderer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ModalRecebimentosImpl extends ModalRecebimentos{

    BigDecimal vlTotalServicos = BigDecimal.ZERO;
    BigDecimal vlTotalReceberDevolver = BigDecimal.ZERO;
    BigDecimal vlTotalRecebido = BigDecimal.ZERO;
    BigDecimal vlTotalDevolvido = BigDecimal.ZERO;
    private Protocolo protocolo;
    private FormaPagamentoService formaPagamentoService;
    private ProtocoloService protocoloService;
    private FinanceiroService financeiroService;

    private List<PagamentoProtocoloFinanceiro> recebimentosList = new ArrayList<>();

    public ModalRecebimentosImpl(Protocolo protocolo) {
        this.financeiroService = BeanLocator.find(FinanceiroService.class);
        this.protocolo = protocolo;
        this.formaPagamentoService = BeanLocator.find(FormaPagamentoService.class);
        this.protocoloService = BeanLocator.find(ProtocoloService.class);


        this.vlTotalServicos = BigDecimal.ZERO;
        if(this.protocolo.getServicos() != null) {
            for (ServicoCalculado servico : this.protocolo.getServicos()) {
                this.vlTotalServicos = this.vlTotalServicos.add(servico.getTotal());
            }
        }
        gridRecebimentos.setStyleGenerator(item -> {
            if(item.getValor().signum() <0){
                return "red";
            }else {
                return null;
            }
        });

        configurarGrid();
//        atualizarValores();
        recalcularTotal();
//        btnAtualizarPagamentosFinanceiro.addClickListener(evt->AtualizarPagamentosFinanceiroListener(evt));
    }

    private void recalcularTotal() {
        this.vlTotalReceberDevolver = BigDecimal.ZERO;

        this.vlTotalDevolvido= BigDecimal.ZERO;
        this.vlTotalRecebido= BigDecimal.ZERO;
        for(PagamentoProtocoloFinanceiro pagamento : this.recebimentosList){
            if(pagamento.getValor().signum() <0){
                this.vlTotalDevolvido = this.vlTotalDevolvido.add(pagamento.getValor());
            }else {
                this.vlTotalRecebido = this.vlTotalRecebido.add(pagamento.getValor());
            }
        }
        this.vlTotalReceberDevolver = this.vlTotalRecebido.add(this.vlTotalDevolvido).subtract(this.vlTotalServicos);

        if(this.vlTotalReceberDevolver.signum() == -1){
            labelTotalReceberDevolver.setValue("Total a Receber");
        }else{
            labelTotalReceberDevolver.setValue("Total a Devolver");
        }

        labelTotalRecebimentos.setValue("R$ " + Utils.getDecimalFormat().format(this.vlTotalRecebido));
        labelTotalDevolucoes.setValue("R$ "+Utils.getDecimalFormat().format(this.vlTotalDevolvido.abs()));


        atualizarValores();
    }
    private void atualizarValores() {
        switch (vlTotalReceberDevolver.compareTo(BigDecimal.ZERO)) {
            case -1:
                labelTotalReceberDevolver.setValue("TOTAL a Receber: ");
                labelTotalReceberDevolver.removeStyleName("red");
//                labelTotalReceberDevolver.addStyleName("colored");
                break;
            case 0:
                labelTotalReceberDevolver.setVisible(false);
                labelTotalReceberDevolverValor.setVisible(false);
                break;
            case 1:
                labelTotalReceberDevolver.setValue("TOTAL a Devolver");
//                labelTotalReceberDevolver.removeStyleName("colored");
                labelTotalReceberDevolver.addStyleName("red");
                break;
        }
        labelTotalReceberDevolverValor.setValue("R$ " + Utils.getDecimalFormat().format(this.vlTotalReceberDevolver.abs()));
    }

    private void configurarGrid(){
        gridRecebimentos.removeAllColumns();
        Grid.Column<PagamentoProtocoloFinanceiro, String> colunaData = gridRecebimentos.addColumn(item -> {
            return Utils.formatarDataComHora(item.getData());
        }, new HtmlRenderer()).setCaption("Dt. Pagamento");
        gridRecebimentos.addColumn(PagamentoProtocoloFinanceiro::getFormaPagamento).setCaption("Forma de Recebimento/Devolução");
        gridRecebimentos.addColumn(item -> {return "R$ "+Utils.getDecimalFormat().format(item.getValor());}, new HtmlRenderer()).setCaption("Valor");
        if(this.protocolo.getNumero() != null){
            recebimentosList.addAll(financeiroService.listarPagamentosProtocolo(this.protocolo.getNumero(), this.protocolo.getTipo()));
            this.vlTotalServicos = financeiroService.somarServicosProtocolo(this.protocolo.getNumero(), this.protocolo.getTipo());
        }

        gridRecebimentos.setItems(recebimentosList);

        gridRecebimentos.setHeightByRows(this.recebimentosList.size()+1);
        gridRecebimentos.sort(colunaData, SortDirection.ASCENDING);
    }
}
