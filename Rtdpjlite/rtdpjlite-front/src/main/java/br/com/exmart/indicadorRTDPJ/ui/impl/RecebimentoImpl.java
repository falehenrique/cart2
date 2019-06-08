package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.Recebimento;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.rtdpjlite.exception.CustaNotPresentException;
import br.com.exmart.rtdpjlite.model.FormaPagamento;
import br.com.exmart.rtdpjlite.model.PagamentoProtocolo;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.service.FinanceiroService;
import br.com.exmart.rtdpjlite.service.FormaPagamentoService;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.vo.financeiro.PagamentoProtocoloFinanceiro;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import br.com.exmart.util.BeanLocator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.textfieldformatter.NumeralFieldFormatter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

//import br.com.exmart.rtdpjlite.model.ServicoProtocolo;

public class RecebimentoImpl extends Recebimento implements PassoProtocolo{

public static String VIEW_NAME = "recebimento";

	BigDecimal vlTotalServicos = BigDecimal.ZERO;
	BigDecimal vlTotalReceberDevolver = BigDecimal.ZERO;
	BigDecimal vlTotalRecebido = BigDecimal.ZERO;
	BigDecimal vlTotalDevolvido = BigDecimal.ZERO;
	private Protocolo protocolo;
	private FormaPagamentoService formaPagamentoService;
	private ProtocoloService protocoloService;
	private FinanceiroService financeiroService;

	private EventBus.SessionEventBus eventBus;
	private List<PagamentoProtocoloFinanceiro> recebimentosList = new ArrayList<>();


	public RecebimentoImpl(Protocolo protocolo) {
		this.eventBus = BeanLocator.find(EventBus.SessionEventBus.class);
		this.financeiroService = BeanLocator.find(FinanceiroService.class);
		this.eventBus.subscribe(this);
		this.protocolo = protocolo;
		this.formaPagamentoService = BeanLocator.find(FormaPagamentoService.class);
		this.protocoloService = BeanLocator.find(ProtocoloService.class);

		comboFormaRecebimento.setItems(this.formaPagamentoService.findAll());

		panelFormaRecebimentoDevolucao.addComponent(valor,1);

		new NumeralFieldFormatter(valor, ".", ",", 2);


		btnAdicionarRecebimento.addClickListener(evt -> adicionarPagamentoRecebimentoClickListener(evt));
		btnAdicionarDevolucao.addClickListener(evt -> adicionarPagamentoDevolucaoClickListener(evt));


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
		atualizarValores();
		recalcularTotal();
		//inicia habilitado
		habilitarForm(true, true);
		btnAtualizarPagamentosFinanceiro.addClickListener(evt->AtualizarPagamentosFinanceiroListener(evt));
	}

	private void AtualizarPagamentosFinanceiroListener(Button.ClickEvent evt) {
		if(this.protocolo.getNumero() != null){
			recebimentosList.clear();
			recebimentosList.addAll(financeiroService.listarPagamentosProtocolo(this.protocolo.getNumero(), this.protocolo.getTipo()));
			gridRecebimentos.getDataProvider().refreshAll();
		}
		atualizarValores();
		recalcularTotal();
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
//		gridRecebimentos.addComponentColumn(pagamento -> {
//			Button button = new Button(VaadinIcons.TRASH);
//			button.addStyleName("borderless");
//			button.addClickListener(click ->
//			{
//				this.recebimentosList.remove(pagamento);
//				this.gridRecebimentos.getDataProvider().refreshAll();
//				recalcularTotal();
//			});
////					Notification.show("Clicked: " + person.toString(), Notification.Type.TRAY_NOTIFICATION));
//			return button;
//		});

		gridRecebimentos.setHeightByRows(this.recebimentosList.size()+1);
//		gridRecebimentos.setColumnOrder(colunaData);
		gridRecebimentos.sort(colunaData, SortDirection.ASCENDING);
	}

	private void atualizarValores() {
		labelTotalServicos.setValue("R$ "+Utils.getDecimalFormat().format(this.vlTotalServicos));
		labelTotalReceberDevolverValor.setValue("R$ " + Utils.getDecimalFormat().format(this.vlTotalReceberDevolver.abs()));
		valor.setValue(Utils.getDecimalFormat().format(this.vlTotalReceberDevolver.abs()));
	}


	private void adicionarPagamentoDevolucaoClickListener(Button.ClickEvent evt) {
		BigDecimal vlInformado = BigDecimal.ZERO;
		try{
			vlInformado = (BigDecimal) Utils.getDecimalFormat().parse(valor.getValue());
			adicionarFormaPagamentoRecebimento(comboFormaRecebimento.getValue(), vlInformado.negate());
		} catch (ParseException e) {
		}
	}
	private void adicionarFormaPagamentoRecebimento(FormaPagamento formaPagamento, BigDecimal vlPagamento){
		if(formaPagamento == null | vlPagamento == null){
			Notification.show("Ops ;) Acho que faltou preencher algum campo", Notification.Type.ERROR_MESSAGE);
		}else {
				PagamentoProtocolo pagamento = new PagamentoProtocolo();
				pagamento.setFormaPagamento(comboFormaRecebimento.getValue());
				pagamento.setValor(vlPagamento);
//				this.protocolo.getPagamentoProtocolo().add(pagamento);
				gridRecebimentos.getDataProvider().refreshAll();
				comboFormaRecebimento.clear();

				recalcularTotal();
				comboFormaRecebimento.clear();
			comboFormaRecebimento.focus();
//			gridRecebimentos.setHeightByRows(this.protocolo.getPagamentoProtocolo().size());
		}
	}
	private void adicionarPagamentoRecebimentoClickListener(Button.ClickEvent evt) {
		BigDecimal vlInformado = BigDecimal.ZERO;
		try{
			vlInformado = (BigDecimal) Utils.getDecimalFormat().parse(valor.getValue());
			adicionarFormaPagamentoRecebimento(comboFormaRecebimento.getValue(), vlInformado);
		} catch (ParseException e) {
		}

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
			comboFormaRecebimento.setCaption("Forma de Recebimento/Devolução");
			labelTotalReceberDevolver.setValue("Total a Receber");
		}else{
			labelTotalReceberDevolver.setValue("Total a Devolver");
			comboFormaRecebimento.setCaption("Forma de Recebimento/Devolução");
		}

		labelTotalRecebimentos.setValue("R$ " + Utils.getDecimalFormat().format(this.vlTotalRecebido));
		labelTotalDevolucoes.setValue("R$ "+Utils.getDecimalFormat().format(this.vlTotalDevolvido.abs()));


		atualizarValores();
	}





    public void setProtocolo(Protocolo protocolo) {
        this.protocolo = protocolo;
    }

	@Override
	public void habilitarForm(boolean habilitar, boolean icProtocoloEditavel) {
		if(!icProtocoloEditavel){
			habilitar = icProtocoloEditavel;
		}
		this.panelFormaRecebimentoDevolucao.setVisible(false);
		this.btnAdicionarRecebimento.setVisible(false);
		this.btnAdicionarDevolucao.setVisible(false);
//		gridRecebimentos.getColumns().get(gridRecebimentos.getColumns().size()-1).setHidden(!habilitar);
		comboFormaRecebimento.focus();
	}

	@Override
	public void cancelar() {

	}

	@Override
	public void focus() {
		comboFormaRecebimento.focus();
	}

	@Override
	public void validate() {

	}

	@Override
	public void removerListener() {
		btnAdicionarRecebimento.removeClickShortcut();
	}

	@Override
	public void addListener() {
		btnAdicionarRecebimento.setClickShortcut(ShortcutAction.KeyCode.ENTER);
	}

//	@EventBusListenerMethod(scope = EventScope.SESSION)
//	public void servicoAdicionado(ProtocoloEvent.ServicoAdicionado evento){
//		try {
//			this.vlTotalServicos = vlTotalServicos.add(evento.getServico().getCustas().get(evento.getServico().getCustas().size()-1).getValor().getAmount());
//		} catch (CustaNotPresentException e) {
//			this.vlTotalServicos = BigDecimal.ZERO;
//		}
//		recalcularTotal();
//	}
//
//	@EventBusListenerMethod(scope = EventScope.SESSION)
//	public void servicoRemovido(ProtocoloEvent.ServicoRemovido evento){
//
//		try {
//			this.vlTotalServicos = vlTotalServicos.subtract(evento.getServico().getCustas().get(evento.getServico().getCustas().size()-1).getValor().getAmount());
//		} catch (CustaNotPresentException e) {
//			this.vlTotalServicos = BigDecimal.ZERO;
//		}
//		recalcularTotal();
//	}


}
