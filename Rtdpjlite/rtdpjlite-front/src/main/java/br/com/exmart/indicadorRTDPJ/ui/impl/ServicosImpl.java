package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.service.rest.vo.ServicoVO;
import br.com.exmart.indicadorRTDPJ.ui.Servicos;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.indicadorRTDPJ.ui.vo.ServicosAgrupadoVO;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.rtdpjlite.vo.financeiro.FormaCalculoFinanceiro;
import br.com.exmart.rtdpjlite.vo.financeiro.PagamentoProtocoloFinanceiro;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoFinanceiro;
import br.com.exmart.util.BeanLocator;
import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus;
import org.vaadin.textfieldformatter.NumeralFieldFormatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServicosImpl extends Servicos implements PassoProtocolo{

	protected static Logger logger= LoggerFactory.getLogger(ServicosImpl.class);
	public static String VIEW_NAME = "servicos";
	private final List<ServicoFinanceiro> servicoList;
	private final List<FormaCalculoFinanceiro> formaCalculoList;
	private FinanceiroService financeiroService;
	private EventBus.ViewEventBus eventBus;
	private ArquivoService arquivoService;
	private FormaCalculoFinanceiro formaCalculoFinanceiroPadrao = null;


	private int qtdCustas = 0;


	br.com.exmart.rtdpjlite.model.Protocolo protocolo;
	private ProtocoloService protocoloService;
	private FormaCalculoService formaCalculoService;
	List<ServicoCalculado> servicoCalculadoList = new ArrayList<>();
	BigDecimal totalPagamentos = BigDecimal.ZERO;
	BigDecimal totalServicos = BigDecimal.ZERO;
	private List<ServicosAgrupadoVO> servicoAgrupadoList = new ArrayList<>();


	public ServicosImpl(br.com.exmart.rtdpjlite.model.Protocolo protocolo, EventBus.ViewEventBus eventBus) {

		this.eventBus = eventBus;
		logger.debug("iniciou ServicosImpl");
		this.protocolo = protocolo;
		this.protocoloService = BeanLocator.find(ProtocoloService.class);
		this.financeiroService = BeanLocator.find(FinanceiroService.class);
		this.formaCalculoService = BeanLocator.find(FormaCalculoService.class);
		this.arquivoService = BeanLocator.find(ArquivoService.class);

		new NumeralFieldFormatter(valorBase, ".", ",", 2);
		new NumeralFieldFormatter(vlDepositoPrevio, ".", ",", 2);
		new NumeralFieldFormatter(repeticoes);
		repeticoes.setValue("1");
		vlDepositoPrevio.addBlurListener(evt->setarDepositoPrevioProtocolo());

		btnAdicionarServico.addClickListener(evt->adicionarServico(evt));
		btnDetalhesCustas.addClickListener(evt ->mostrarDetalhes());

		formaCalculo.setItemCaptionGenerator(FormaCalculoFinanceiro::getNmDivisor);
		formaCalculoList = financeiroService.listarFormaCalculo();
		formaCalculo.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},formaCalculoList);

		for(FormaCalculoFinanceiro formaCalculo : this.formaCalculoList){
			if(formaCalculo.getNmDivisor().toUpperCase().contains("INTEGRAIS")){
				formaCalculoFinanceiroPadrao = formaCalculo;
			}
		}


		servicoList = financeiroService.listarServicos(this.protocolo.getTipo());
		if(this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_TD) || this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_PJ)){
			servico.setItems(servicoList.stream().filter(item -> item.isIcCertidao() == true));
		}else{
			servico.setItems(servicoList.stream().filter(item -> item.isIcCertidao() == false));
		}

		servico.setItemCaptionGenerator(ServicoFinanceiro::getNmServico);
		if(protocolo.getStatus() != null){
			//TODO deve verificar se vai continuar apenas esse status
			if(protocolo.getStatus().getStatus().getNome().equalsIgnoreCase("DEVOLVIDO")){
				gridServicos.addStyleName("gridTituloDevolvido");
			}
		}
		servico.addValueChangeListener(evt->servicoChangeListener(evt));
		//inicia habilitado
		habilitarForm(true, true);
		montarGridServicos();
//		habilitarDesabilitarForm();

		calcaulaTotal();
        if(protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_TD) || protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_PJ)) {
			btnVerMovimentacoes.setVisible(false);
			labelTotalReceberDevolver.setVisible(false);
			labelTotalReceberDevolverValor.setVisible(false);
        }
		btnVerMovimentacoes.addClickListener(evt -> btnVerMovimentacoesClickListener());
		btnAtualizarPagamentos.addClickListener(evt->btnAtualizarPagamentosClickListener());
		servico.setPageLength(0);
		agruparServicos();
		logger.debug("finalizou ServicosImpl");
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		System.out.println(nf.format(protocolo.getVlDepositoPrevio()));
		vlDepositoPrevio.setValue(nf.format(protocolo.getVlDepositoPrevio()).replace(",","").replace(".",","));
//		vlDepositoPrevio.setVisible(false);
	}

	private void setarDepositoPrevioProtocolo() {
		BigDecimal vlDepositoPrevioInformado = BigDecimal.ZERO;
		try {
			Money vlDeposito = Utils.convertToMoney(this.vlDepositoPrevio.getValue());
			vlDepositoPrevioInformado = vlDeposito.getAmount();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.protocolo.setVlDepositoPrevio(vlDepositoPrevioInformado);
	}

	private void btnAtualizarPagamentosClickListener() {
		atualizarPagamentos();
	}

	private void btnVerMovimentacoesClickListener() {
		GerenciarJanela.abrirJanela("RECEBIMENTOS DO PROTOCOLO",90,95,new ModalRecebimentosImpl(this.protocolo),null, true);
	}

	private void servicoChangeListener(HasValue.ValueChangeEvent<ServicoFinanceiro> evt) {
		if(evt.getValue() != null) {
			//TODO validar
//			if (evt.getValue().isQuantidade()) {
//				valorBase.setValue("1");
//			} else {
//				valorBase.setValue("0");
//			}
//		}else{
			valorBase.setValue("0");
		}
	}

	int contador= 0;
	int contadorCustas = 0;
	private void montarGridServicos() {

		gridServicos.removeAllColumns();

		gridServicos.addColumn(ServicosAgrupadoVO::getNmServico).setCaption("Serviço").setMinimumWidthFromContent(true);

		gridServicos.addColumn(item -> {return "R$ "+Utils.getDecimalFormat().format(item.getVlBase());}, new HtmlRenderer()).setCaption("Vl. Base") .setMinimumWidthFromContent(true);

		gridServicos.addColumn(ServicosAgrupadoVO::getQtd).setCaption("Qtd.") .setMinimumWidthFromContent(true);
		gridServicos.addColumn(ServicosAgrupadoVO::getFormaCalculo).setCaption("Forma Cálculo") .setMinimumWidthFromContent(true);



		gridServicos.addColumn(item -> {return "R$ "+Utils.getDecimalFormat().format(item.getTotal());}, new HtmlRenderer()).setCaption("Vl. Total") .setMinimumWidthFromContent(true);

		gridServicos.addComponentColumn(servico -> {
			Button button = new Button(VaadinIcons.TRASH);
			button.addStyleName("borderless");
			button.addClickListener(click -> {
				String id = this.getGroupingByKeyVo(servico);
				List<ServicoCalculado> remover = new ArrayList<>();
				for(ServicoCalculado servicoCalculado : this.servicoCalculadoList){
					if(id.equalsIgnoreCase(this.getGroupingByKey(servicoCalculado))) {
						if (servicoCalculado.getIdServicoCalculado() == null) {
							remover.add(servicoCalculado);
						} else if (financeiroService.excluirServico(servicoCalculado.getIdServicoCalculado())) {
							remover.add(servicoCalculado);
						}
					}
				}
				this.servicoCalculadoList.removeAll(remover);
				calcaulaTotal();
				agruparServicos();
//				else{
//					Notification.show("Erro ao excluir serviço, favor entrar em contato com o Administrador", Notification.Type.ERROR_MESSAGE);
//				}
				this.eventBus.publish(this, new ProtocoloEvent.ServicosAtualizado(this.servicoCalculadoList));
			});
//					Notification.show("Clicked: " + servico.toString(), Notification.Type.TRAY_NOTIFICATION));
			return button;
		}).setWidth(100);
		if(this.protocolo.getNumero() != null){
            if(!protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_TD) && !protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_PJ)) {
                List<ServicoCalculado> retorno = financeiroService.listarServicosProtocolo(this.protocolo.getNumero(), protocolo.getTipo());
                this.servicoCalculadoList.addAll(retorno);
                this.eventBus.publish(this, new ProtocoloEvent.ServicosAtualizado(this.servicoCalculadoList));
            }else{
                servicoCalculadoList.addAll(this.protocolo.getServicos());
            }
        }
		gridServicos.setItems(servicoAgrupadoList);

	}

	private String getGroupingByKeyVo(ServicosAgrupadoVO servico) {
		return servico.getIdServico()+"-"+servico.getFormaCalculo();
	}


	private String recuperaNomeServico(Long idServico) {
		for(ServicoFinanceiro servico : servicoList){
			if(servico.getIdServico().equals(idServico)){
				return servico.getNmServico();
			}
		}
		return null;
	}

	private String recuperaFormaCalculo(Integer cdFormaCalculo) {
		for(FormaCalculoFinanceiro formaCalculoFinanceiro : this.formaCalculoList){
			if(formaCalculoFinanceiro.getCdDivisor().equals(cdFormaCalculo)){
				return formaCalculoFinanceiro.getNmDivisor();
			}
		}
		return null;
	}


	private void adicionarServico(Button.ClickEvent evt) {
		Integer repetir = 1;
		try{
			repetir = Integer.parseInt(repeticoes.getValue());
		}catch (Exception e){

		}
		if(servico.isEmpty() | formaCalculo.isEmpty() | valorBase.isEmpty()){
			Notification.show("Ops ;) Acho que faltou preencher algum campo", Notification.Type.WARNING_MESSAGE);
			servico.focus();
		}else{

			try {
				Money valor = Utils.convertToMoney("0");
				Integer quantidade = 0;
				//TODO Validar
//				if (servico.getValue().isQuantidade()) {
//					quantidade = Integer.parseInt(valorBase.getValue().replace(".","").replace(",",""));
//				}else{
					valor = Utils.convertToMoney(valorBase.getValue());
//				}

				//FIXME executar apenas 1 vez  para nao buscar varias vezes no ci
				for (int x=1; x<=repetir; x++){
					ServicoCalculado retorno = financeiroService.calcularServico(servico.getValue().getIdServico(), valor.getAmount(), formaCalculo.getValue().getCdDivisor(), protocolo.getTipo());
					retorno.setNmFormaCalculo(formaCalculo.getValue().getNmDivisor());
					servicoCalculadoList.add(retorno);
				}

				agruparServicos();

				calcaulaTotal();
				limparFormulario();
				this.eventBus.publish(this, new ProtocoloEvent.ServicosAtualizado(this.servicoCalculadoList));
				servico.focus();
				gridServicos.setHeightByRows(this.servicoAgrupadoList.size());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		this.protocolo.setServicos(this.servicoCalculadoList);
	}

	private void agruparServicos() {
		Map<String, List<ServicoCalculado>> re = servicoCalculadoList.stream().collect(Collectors.groupingBy(o -> getGroupingByKey(o), Collectors.toList()));
		this.servicoAgrupadoList.clear();
//		List<ServicosAgrupadoVO> servicosAgrupados = new ArrayList<>();
		for(Map.Entry<String, List<ServicoCalculado>> entry : re.entrySet()){
			ServicoVO servico = new ServicoVO();
			BigDecimal vlTotal = BigDecimal.ZERO;
			for(ServicoCalculado servicoCal : entry.getValue()){
				vlTotal = vlTotal.add(servicoCal.getTotal());
			}
			this.servicoAgrupadoList.add(new ServicosAgrupadoVO(
					entry.getValue().get(0).getIdServico(),
					entry.getValue().get(0).getNmServico(),
					vlTotal,
					entry.getValue().get(0).getVlBase(),
					entry.getValue().get(0).getNmFormaCalculo(),
					entry.getValue().size()
			));
		}
		this.servicoAgrupadoList.sort(Comparator.comparing(ServicosAgrupadoVO::getNmServico));
		gridServicos.getDataProvider().refreshAll();
	}

	private String getGroupingByKey(ServicoCalculado p){
		return p.getIdServico()+"-"+p.getNmFormaCalculo();
	}



	private void limparFormulario() {
		valorBase.setValue("0");
		servico.clear();
		repeticoes.setValue("1");
		formaCalculo.setValue(this.formaCalculoFinanceiroPadrao);
	}


	@Override
	public void focus() {
		atualizarPagamentos();
		formaCalculo.setValue(this.formaCalculoFinanceiroPadrao);
		servico.focus();
	}

	private void atualizarPagamentos() {
		totalPagamentos = BigDecimal.ZERO;
		List<PagamentoProtocoloFinanceiro> pagamentos = financeiroService.listarPagamentosProtocolo(this.protocolo.getNumero(), this.protocolo.getTipo());
		for(PagamentoProtocoloFinanceiro pagamento : pagamentos){
			totalPagamentos = totalPagamentos.add(pagamento.getValor());
		}
		labelTotalReceberDevolverValor.setValue("R$ " + Utils.getDecimalFormat().format(totalPagamentos));

		BigDecimal resultadoCalculo = totalServicos.subtract(totalPagamentos);
		labelResultadoCalculo.setVisible(true);
		labelResultadoCalculoValor.setVisible(true);
		switch (resultadoCalculo.compareTo(BigDecimal.ZERO)) {
			case -1:
				labelResultadoCalculo.setValue("TOTAL a Devolver");
				labelResultadoCalculo.removeStyleName("colored");
				labelResultadoCalculo.addStyleName("red");
				break;
			case 0:
				labelResultadoCalculo.setVisible(false);
				labelResultadoCalculoValor.setVisible(false);
				break;
			case 1:
				labelResultadoCalculo.setValue("TOTAL a Receber: ");
				labelResultadoCalculo.removeStyleName("red");
				labelResultadoCalculo.addStyleName("colored");
				break;
		}
		labelResultadoCalculoValor.setValue("R$ " + Utils.getDecimalFormat().format(resultadoCalculo.abs()));

	}

	@Override
	public void validate() {

	}

	private void calcaulaTotal() {
		totalServicos = BigDecimal.ZERO;
		BigDecimal custas = BigDecimal.ZERO;
		for(ServicoCalculado servico : this.servicoCalculadoList){
			totalServicos = totalServicos.add(servico.getTotal());
		}

		labelTotalServicosValor.setValue("R$ "+ Utils.getDecimalFormat().format(totalServicos));
		atualizarPagamentos();
	}


	private void mostrarDetalhes(){
		GerenciarJanela.abrirJanela("DETALHAMENTO DAS CUSTAS",90, 100, new ModalDetalhesCustasImpl(this.servicoCalculadoList, ( (MyUI)  UI.getCurrent()).getCustasCartorio()));
	}


	public void cancelar() {
		this.protocolo.getServicos().clear();
	}

	private boolean verificaProtocolo() {
		return this.protocolo.getNumero() != null;
	}


	public void setProtocolo(Protocolo protocolo) {
		this.protocolo = protocolo;
	}

	@Override
	public void habilitarForm(boolean habilitar, boolean icProtocoloEditavel) {
	    if(!icProtocoloEditavel){
	        habilitar = icProtocoloEditavel;
        }
		panelServico.setVisible(habilitar);
	    vlDepositoPrevio.setEnabled(habilitar);
		gridServicos.getColumns().get(gridServicos.getColumns().size()-1).setHidden(!habilitar);
		servico.focus();
	}

	@Override
	public void removerListener() {
		btnAdicionarServico.removeClickShortcut();
	}

	@Override
	public void addListener() {
		btnAdicionarServico.setClickShortcut(ShortcutAction.KeyCode.ENTER);
	}

	public List<ServicoCalculado> getServicosCalculados() {
		return this.servicoCalculadoList;
	}

	public void disparaEventBus() {
		this.eventBus.publish(this, new ProtocoloEvent.ServicosAtualizado(this.servicoCalculadoList));
	}

    public void prepararPrenotarExameCalculo() {
		for(ServicoCalculado servico : this.servicoCalculadoList){
			servico.setIdServicoCalculado(null);
		}
    }
}
