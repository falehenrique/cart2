
package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.Protocolo;
import br.com.exmart.indicadorRTDPJ.ui.custom.MyStringToDateConverter;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.MyStringToMoneyConverter;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.indicadorRTDPJ.ui.util.*;
import br.com.exmart.indicadorRTDPJ.ui.view.ModalVisualizarDocumento;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.model.portal.UsuarioPortal;
import br.com.exmart.rtdpjlite.repository.StatusRepository;
import br.com.exmart.rtdpjlite.repository.portal.UsuarioPortalRepository;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.rtdpjlite.service.enums.Diretorio;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;
import br.com.exmart.rtdpjlite.vo.balcaoonline.PedidoMensagemTipo;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.*;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter;
import org.vaadin.textfieldformatter.NumeralFieldFormatter;
import org.vaadin.textfieldformatter.PhoneFieldFormatter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProtocoloImpl extends Protocolo implements PassoProtocolo{
	protected static Logger logger= LoggerFactory.getLogger(ProtocoloImpl.class);
	private BeanValidationBinder<br.com.exmart.rtdpjlite.model.Protocolo> binderProtocolo = new BeanValidationBinder<>(br.com.exmart.rtdpjlite.model.Protocolo.class);
	public static String VIEW_NAME = "protocolo";
    private br.com.exmart.rtdpjlite.model.Protocolo protocolo;
    private List<Status> statusProtocoloList= new ArrayList<>();
    private ClienteService clienteService;


	private NavigationManager navigationManager;
	private ProtocoloService protocoloService;
	private final NaturezaService naturezaService;
	private final UsuarioService usuarioService;
	private final FormaEntregaService formaEntregaService;
	private final ParteService parteService;
	private CalcularPrazoProtocoloService calcularPrazoProtocolo;
	private EventBus.ViewEventBus eventBus;
	private RegistroService registroService;
	private SubNaturezaService subNaturezaService;
	private CartorioParceiroService cartorioParceiroService;
	private ArquivoService arquivoService;

	private ReverterService reverterService;

	private UsuarioPortalRepository usuarioPortalRepository;



	// MÉTODOS MENU ANDAMENTO //
	public void mostrarEdicao() {
		panelAdd.setVisible(!panelAdd.isVisible());
		panelView.setVisible(!panelView.isVisible());
	}
	// MÉTODOS DE NATUREZAS ESPECÍFICAS //
	public void mostrarDatasExame(){ panelAddDatasExame.setVisible(!panelAddDatasExame.isVisible()); }
	public void mostrarDatasPrenotacao(){ panelAddDatasPrenotacao.setVisible(!panelAddDatasPrenotacao.isVisible()); }
	public void esconderDadosProtocolo(){ panelAddProtocolo.setVisible(!panelAddProtocolo.isVisible()); }


	public void esconderPastaPJ(){ pastaPj.setVisible(!pastaPj.isVisible()); }

	public ProtocoloImpl(br.com.exmart.rtdpjlite.model.Protocolo protocolo, EventBus.ViewEventBus eventBus, String registroReferencia, Long idCliente, String formaEntrega, Long idPedido) {
		logger.debug("iniciou ProtocolImpl");

		this.protocoloService =  BeanLocator.find(ProtocoloService.class);
		this.reverterService = BeanLocator.find(ReverterService.class);
		this.navigationManager  = BeanLocator.find(NavigationManager.class);
		this.naturezaService = BeanLocator.find(NaturezaService.class);
		this.usuarioService = BeanLocator.find(UsuarioService.class);
		this.formaEntregaService  = BeanLocator.find(FormaEntregaService.class);
		this.parteService  = BeanLocator.find(ParteService.class);
		this.calcularPrazoProtocolo  = BeanLocator.find(CalcularPrazoProtocoloService.class);
		this.eventBus = eventBus;
		this.statusProtocoloList = ((MyUI) UI.getCurrent()).getStatus();
		this.registroService = BeanLocator.find(RegistroService.class);
		this.clienteService = BeanLocator.find(ClienteService.class);
		this.subNaturezaService = BeanLocator.find(SubNaturezaService.class);
		this.cartorioParceiroService = BeanLocator.find(CartorioParceiroService.class);
		this.protocolo = protocolo;
		this.arquivoService = BeanLocator.find(ArquivoService.class);
		this.usuarioPortalRepository = BeanLocator.find(UsuarioPortalRepository.class);

		panelExaminado.setVisible(false);
		if(this.protocolo.getNumero() == null){
			this.protocolo.setVias(1);
			this.protocolo.setValor(Money.zero(Utils.BRL));
			this.protocolo.setDataDoc(LocalDate.now());
		}
		if(this.protocolo.getPedido() == null) {
			this.protocolo.setPedido(idPedido);
		}
		panelCertidaoEmitida.setVisible(false);
		if(this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_PJ) || this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_TD)) {
			numeroReg.setCaption("Nº do Registro");
			valorDocumento.setVisible(false);
			dtDocumento.setVisible(false);
			subNaturezaTitulo.setVisible(false);
		}else{
			numeroReg.setCaption("Nº do Registro Anterior");
			valorDocumento.setVisible(true);
			dtDocumento.setVisible(true);
			subNaturezaTitulo.setVisible(true);
		}

		if(this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_TD) || this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_PJ)){
			List<Natureza> naturezaTituloList = naturezaService.findAllByTipo(2L);
			naturezaTitulo.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},naturezaTituloList);//TODO CERTIDAO -- DEVE MELHORAR ISSO
			numeroVias.setVisible(false);
//			naturezaTitulo.setPageLength(naturezaTituloList.size());
		}else {
			List<Natureza> naturezaTituloList = naturezaService.findAllByTipo(1L);
            naturezaTitulo.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},naturezaTituloList);//TODO Exceto CERTIDAO -- DEVE MELHORAR ISSO
//			naturezaTitulo.setPageLength(naturezaTituloList.size());
		}


		naturezaTitulo.setItemCaptionGenerator(Natureza::getNome);
		subNaturezaTitulo.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getSubNaturezas());
		subNaturezaTitulo.setItemCaptionGenerator(SubNatureza::getNome);

		comboCliente.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},clienteService.findAll());
		comboCliente.setItemCaptionGenerator(Cliente::getNome);

		comboResponsavel.setItemCaptionGenerator(UsuarioPortal::getNome);
		if(this.protocolo.getResponsavelPedido() == null){
			comboResponsavel.setVisible(false);
		}


		new PhoneFieldFormatter(apresentanteTelefone, "BR");
		new CustomStringBlockFormatter(dtDocumento, new int[]{2,2,4}, new String[]{"/","/"}, CustomStringBlockFormatter.ForceCase.NONE);
		new NumeralFieldFormatter(valorDocumento, ".", ",", 2);

		//iniciar habilitado
		habilitarForm(true, true);
		naturezaTitulo.setTextInputAllowed(true);
		naturezaTitulo.setEmptySelectionAllowed(false);

		entrega.setItemCaptionGenerator(FormaEntrega::getNome);
		List<FormaEntrega> formaEntregaList = ((MyUI) UI.getCurrent()).getFormaEntregas();
		if(!TipoProtocolo.isCertidao(this.protocolo.getTipo())){
			formaEntregaList = formaEntregaList.stream().filter(forma -> !forma.getNome().equalsIgnoreCase("ENCAMINHAR PARA OUTRO CARTÓRIO")).collect(Collectors.toList());
		}

		entrega.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},formaEntregaList);

		escrevente.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},usuarioService.getEscreventes());
		escrevente.setItemCaptionGenerator(Usuario::getNome);


		verificarEspecialidade();
		bindCampos();

		checkGuarda.addValueChangeListener(evt-> checkGuardaValueChangeListener(evt));
		naturezaTitulo.addValueChangeListener(evt->naturezaTituloValueChangeListener(evt));
		docParte.addBlurListener(evt -> buscarParte(evt));
		panelRegistrado.setVisible(false);
		panelDevolvido.setVisible(false);

		panelRegistrado_btnVerRegistro.addClickListener(evt->verRegistroListener(evt));
		panelRegistrado_btnEntregue.addClickListener(evt->entregarProtocolo());
		panelRegistrado_btnVerCertidaoIntimacao.addClickListener(evt->visualizarCertidaoIntimacao(evt));

		if(this.protocolo.getTipo().equals(TipoProtocolo.PRENOTACAO_PJ) || this.protocolo.getTipo().equals(TipoProtocolo.PRENOTACAO_TD)) {
			if (protocolo.getNumeroRegistro() != null) {
				panelRegistrado_nrRegistro.setValue(protocolo.getNumeroRegistro());
				for (StatusProtocolo status : this.protocolo.getStatusProtocolo()) {
					if (status.getStatus().getNome().equalsIgnoreCase("REGISTRADO")) {
						mostrarPanelRegistrado(status);
					}
				}
			}
		}else if(this.protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_PJ) || this.protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_TD)){
			for (StatusProtocolo status : this.protocolo.getStatusProtocolo()) {
				if (status.getStatus().getNome().equalsIgnoreCase("APTO PARA REGISTRO")) {
					mostrarPanelExaminado(status);
				}
			}
        }else if(this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_PJ) || this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_TD)){
			for (StatusProtocolo status : this.protocolo.getStatusProtocolo()) {
				if (status.getStatus().getNome().equalsIgnoreCase("PRONTO PARA ENTREGA")) {
					mostrarPanelCertidao(status);
				}
			}
		}

		boolean icMostrarPanelDevolvido = false;
		boolean icReentrada = false;
		StatusProtocolo statusDevolvido = null;
		List<StatusProtocolo> listaStatus = this.protocolo.getStatusProtocolo().stream().sorted(Comparator.comparing(d -> d.getData())).collect(Collectors.toList());
		Long idStatus = null;
		for (StatusProtocolo status : listaStatus ) {
			if (status.getStatus().getNome().equalsIgnoreCase("DEVOLVIDO")) {
				statusDevolvido = status;
				icMostrarPanelDevolvido = true;
				idStatus = status.getId();
			}else if(status.getStatus().getNome().equalsIgnoreCase("REENTRADA")) {
				icMostrarPanelDevolvido = false;
				icReentrada = false;
//				break;
			}else if(status.getStatus().getNome().equalsIgnoreCase("ENTREGUE")) {
				icReentrada = true;
			}
		}
		if(icMostrarPanelDevolvido){
			mostrarPanelDevolvido(statusDevolvido, icReentrada);
			BrowserWindowOpener opener = new BrowserWindowOpener("/protocolo/" +this.protocolo.getTipo().getId()+ "/"+ this.protocolo.getNumero() + "/devolucao/"+idStatus);
			opener.extend(panelDevolvido_btnVerDevolucao);

		}


//		btnVerRegistroReferencia.addClickListener(evt->visualizarRegistroReferencia());

		if(!Strings.isNullOrEmpty(registroReferencia)){
			Registro registroRef = registroService.findByRegistroAndEspecialidade(registroReferencia, TipoProtocolo.recuperaEspecialidade(this.protocolo.getTipo()));
			if(registroRef != null){
				this.protocolo.setNumeroRegistroReferencia(registroRef.getNumeroRegistroReferencia());
				this.protocolo.setRegistroReferencia(registroRef.getRegistro());
				if(!Strings.isNullOrEmpty(registroRef.getNrPastaPj())) {
					pastaPj.setValue(registroRef.getNrPastaPj());
				}
//				this.protocolo.setPastaPJ(registroRef.getNrPastaPj());
				numeroReg.setValue(registroRef.getNumeroRegistro().toString());
			}else{
				Notification.show("Atenção", "Registro referência de nº: "+registroReferencia +" não encontrado", Notification.Type.ERROR_MESSAGE);
			}
		}

		numeroReg.addBlurListener(evt-> verificarRegistroAnterior(evt));


		if(protocolo.getTipo().equals(TipoProtocolo.PRENOTACAO_TD)){
			panelGuardaConservacao.setVisible(true);
		}else{
			panelGuardaConservacao.setVisible(false);
		}

		this.eventBus.subscribe(this);

		panelDevolvido_btnReentrada.addClickListener(evt->{GerenciarJanela.abrirJanela(null, 65, 70, new ModalReentradaImpl(this.protocolo, statusProtocoloList.stream().filter(item -> {return  item.getNome().equalsIgnoreCase("REENTRADA");}).collect(Collectors.toList()).get(0)), null, false);});


		if(idCliente != null){
			Cliente clienteSelecionado =clienteService.findOne(idCliente);
			protocolo.setCliente(clienteSelecionado);
			comboCliente.setValue(clienteSelecionado);
		}
		if(!Strings.isNullOrEmpty(formaEntrega)){
			for(FormaEntrega forma: formaEntregaList){
				if(forma.getNome().startsWith(formaEntrega.toUpperCase())) {
					entrega.setValue(forma);
				}
			}
		}
		parte.addBlurListener(evt-> setarApresentante(evt));

		BrowserWindowOpener opener = new BrowserWindowOpener("/protocolo/" +this.protocolo.getTipo().getId()+"/"+ this.protocolo.getNumero() + "/apto");
		opener.extend(panelExaminado_btnVerExame);
		naturezaTitulo.setPageLength(1000);
		subNaturezaTitulo.setPageLength(1000);
		comboCliente.setPageLength(1000);
		entrega.setPageLength(1000);

		btnReverterRegistro.addClickListener(evt->reverterRegistro(evt));
        btnReverterCertidao.addClickListener(evt->reverterCertidao(evt));
		logger.debug("finalizou ProtocolImpl");
	}

    private TextArea motivo = new TextArea("Motivo");
	private void reverterRegistro(Button.ClickEvent evt) {
        motivo.clear();
		MessageBox.create().withMessage(motivo)
				.withCaption("Reverter Registro")
				.withYesButton(() -> {
				    if(Strings.isNullOrEmpty(motivo.getValue())){
                        Notification.show("Atenção", "Favor informar o motivo", Notification.Type.ERROR_MESSAGE);
                    }else {
                        Registro registro = registroService.findByProtocoloIdAndEspecialidade(this.protocolo.getId(), TipoProtocolo.recuperaEspecialidade(this.protocolo.getTipo()));
                        try {
                            reverterService.reverterRegistro(protocolo,registro, motivo.getValue(), ((MyUI) UI.getCurrent()).getUsuarioLogado());
                            navigationManager.navegarPara(this.protocolo.getTipo(), protocolo.getNumero());
                        } catch (Exception e) {
                            Notification.show("Atenção", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                        }
                    }
                },ButtonOption.caption("Reverter"), ButtonOption.focus())
				.withNoButton(ButtonOption.caption("Cancelar"))
				.open();
	}


    private void reverterCertidao(Button.ClickEvent evt) {
        motivo.clear();
        MessageBox.create().withMessage(motivo)
                .withCaption("Reverter registro")
                .withYesButton(() -> {
                    if(Strings.isNullOrEmpty(motivo.getValue())){
                        Notification.show("Atenção", "Favor informar o motivo", Notification.Type.ERROR_MESSAGE);
                    }else {
                        try {
                            reverterService.reverterProtocolo(this.protocolo, motivo.getValue(), ((MyUI) UI.getCurrent()).getUsuarioLogado());
                            navigationManager.navegarPara(this.protocolo.getTipo(), protocolo.getNumero());
                        } catch (Exception e) {
                            Notification.show("Atenção", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                        }
                    }
                },ButtonOption.caption("Reverter"), ButtonOption.focus())
                .withNoButton(ButtonOption.caption("Cancelar"))
                .open();
    }

	private void visualizarCertidaoIntimacao(Button.ClickEvent evt) {
		GerenciarJanela.abrirJanela("Documento do Pedido", 90, 60, new ModalVisualizarDocumento(arquivoService.existeArquivoProtocoloCarimbado(this.protocolo.getNumero(), ((MyUI) UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), Diretorio.CERTIDAO_NOTIFICACAO, protocolo.getTipo())));
	}


	private void setarApresentante(FieldEvents.BlurEvent evt) {
		if(!Strings.isNullOrEmpty(parte.getValue())){
			if(Strings.isNullOrEmpty(apresentante.getValue()))
				apresentante.setValue(parte.getValue());
		}

	}

	@EventBusListenerMethod(scope = EventScope.VIEW)
	public void retroativoCadastrado(ProtocoloEvent.RetroativoCadastrado event){
		setarRetroativo(event.getRegistro());
	}


	private void checkGuardaValueChangeListener(HasValue.ValueChangeEvent<Boolean> evt) {
		if(checkGuarda.isEnabled()){
			checkSigilo.setEnabled(evt.getValue());
			if(evt.getValue() == false){
				checkSigilo.setValue(false);
			}
		}

		try {
			Long pedidoId = 0L;
			Long pedidoProtocoloId = 0L;
			Long remetente = 0l;
			Long destinatario = 0l;
			Long clienteid = 0l;
			Long idTipo = 0l;
			String numero = "";
//FIXME entender			TipoMensagem tipoMensagem = BeanLocator.find(TipoMensagemRepository.class).findOne(idTipo);
//			Mensagem mensagem = BeanLocator.find(PedidoService.class).criarNovaMensagem(remetente, destinatario, clienteid, tipoMensagem, "Conteudo", numero);
//			BeanLocator.find(PedidoService.class).enviarMensagem(pedidoId, pedidoProtocoloId, mensagem);
		}catch (Exception e){
			e.printStackTrace();
		}
	}



	private void verificarRegistroAnterior(FieldEvents.BlurEvent evt) {
//		if(this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_TD) || this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_PJ)){
		if(!Strings.isNullOrEmpty(numeroReg.getValue())) {
			List<Registro> protocoloReferenciaList = registroService.findByNumeroRegistroAndEspecialidade(new Long(numeroReg.getValue()), this.protocolo.getTipo());
			if (protocoloReferenciaList.size() == 0 || arquivoService.isDocumentoRegistroExistis(protocoloReferenciaList.get(0), this.protocolo.getTipo()) == false) {
				Long ultimoRegistro = registroService.findMaxByEspecialidade(TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()));
				if(new Long(numeroReg.getValue()) < ultimoRegistro){
					MessageBox
						.createQuestion()
						.withCaption("Atenção")
						.withMessage("Registro ou imagem do registro nº: " + numeroReg.getValue() + " não encontrado. Deseja cadastrar o retroativo?")
						.withYesButton(() -> {
							GerenciarJanela.abrirJanela("CADASTRO RETROATIVO", 90, 80, new ModalRetroativoImpl(new Long(numeroReg.getValue()), TipoProtocolo.recuperaEspecialidade(this.protocolo.getTipo()), this.eventBus), null, true, true, true, true);
						}, ButtonOption.caption("SIM"), ButtonOption.focus())
						.withNoButton(ButtonOption.caption("NÃO"))
						.open();
				}else{
					Notification.show("Atenção","Registro informado (" + new Long(numeroReg.getValue())+ ") não pode ser maior do que o ultimo registro efetuado ("+ultimoRegistro+")", Notification.Type.ERROR_MESSAGE);
				}

			}else if (protocoloReferenciaList.size() == 1) {
				Registro protocoloReferencia = protocoloReferenciaList.get(0);
				setarRetroativo(protocoloReferencia);
				if(this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_PJ) || this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_TD)) {
					if (protocoloReferencia.getIcPossuiSigiloLegal()) {
						MessageBox
								.createQuestion()
								.withCaption("Atenção")
								.withMessage("Registro número: " + protocoloReferencia.getRegistro() + " possui Sigilo Legal, deseja continuar?")
								.withYesButton(ButtonOption.caption("SIM"), ButtonOption.focus())
								.withNoButton(() -> {
									setarRetroativo(null);
								}, ButtonOption.caption("NÃO"))
								.open();
					}
				}
			} else {
				Notification.show("Atenção", "Encontrado mais de um registro para o numero informado", Notification.Type.ERROR_MESSAGE);
			}
		}
//		}
	}


	private void entregarProtocolo(){
		for(Status status : this.statusProtocoloList){
			if(status.getNome().equalsIgnoreCase("ENTREGUE")){
				GerenciarJanela.abrirJanela(null, 65, 70, new ModalEntregaImpl(this.protocolo, status), null, false);
			}
		}
	}
	private void mostrarPanelCertidao(StatusProtocolo status) {
		panelCertidaoEmitida.setVisible(true);
		panelCertidaoEmitida_btnVerCertidao.addClickListener(evt -> {
			try{
				GerenciarJanela.abrirJanela("Documento do Pedido", 90, 60, new ModalVisualizarDocumento(protocoloService.recuperarArquivoCertidaoAssinadoFile(this.protocolo)));
			}catch (Exception e ){
				Notification.show("Atenção", "Arquivo não encontrado", Notification.Type.ERROR_MESSAGE);
			}
		});

		panelCertidaoEmitida_dtEmissao.setValue(Utils.formatarDataComHora(status.getData()));
		panelCertidaoEmitida_btnEntregueCertidao.addClickListener(evt->entregarProtocolo());
		for (StatusProtocolo statusProtocolo : this.protocolo.getStatusProtocolo()) {
			if(statusProtocolo.getStatus().getNome().equalsIgnoreCase("ENTREGUE")) {
				panelCertidaoEmitida_btnEntregueCertidao.setVisible(false);
			}
		}


	}

	private void mostrarPanelExaminado(StatusProtocolo status) {
		panelExaminado.setVisible(true);
		panelExaminado_dtExaminado.setValue(Utils.formatarDataComHora(status.getData()));
		boolean icEntregue = false;
		for (StatusProtocolo statusProtocolo : this.protocolo.getStatusProtocolo()) {
			if(statusProtocolo.getStatus().getNome().equalsIgnoreCase("ENTREGUE")) {
				icEntregue = true;
			}
		}
		panelExaminado_btnEntregue.addClickListener(evt->entregarProtocolo());
		panelExaminado_btnEntregue.setVisible(!icEntregue);
		panelExaminado_btnProtocolar.setEnabled(icEntregue);
		panelExaminado_btnProtocolar.addClickListener(evt->panelExaminado_btnProtocolarListener(evt));
	}

	private void panelExaminado_btnProtocolarListener(Button.ClickEvent evt) {
		MessageBox
				.createQuestion()
				.withCaption("Atenção")
				.withMessage("Deseja realmente transformar este protocolo de Exame e Cálculo em um protocolo de Registro?")
				.withYesButton(() -> {this.eventBus.publish(this, new ProtocoloEvent.ProtocolarExameCalculo(this.protocolo));;}, ButtonOption.caption("SIM"), ButtonOption.focus())
				.withNoButton(ButtonOption.caption("NÃO"))
				.open();
	}


	private void verRegistroListener(Button.ClickEvent evt) {
		//FIXME deve recuerar corretamente
		Registro registro = registroService.findByProtocoloIdAndEspecialidade(this.protocolo.getId(), TipoProtocolo.recuperaEspecialidade(this.protocolo.getTipo()));
//		reverterService.reverterRegistro(registro, "motivooo", ((MyUI) UI.getCurrent()).getUsuarioLogado());
//		reverterService.reverterProtocolo(this.protocolo.getNumero(), this.protocolo.getTipo(), "motivooo", ((MyUI) UI.getCurrent()).getUsuarioLogado());
		GerenciarJanela.abrirJanela("", 100, 100, new ViewRegistroImpl(registro));
	}

	public void mostrarPanelRegistrado(StatusProtocolo status){
		//TODO COLOCAR ACOES DOS BOTOES
		panelRegistrado_dtRegistro.setValue(Utils.formatarDataComHora(status.getData()));
		panelRegistrado.setVisible(true);
		panelRegistrado_btnVerRegistro.setEnabled(true);
		panelDevolvido.setVisible(false);
		panelRegistrado_btnVerCertidaoIntimacao.setVisible(false);


		if(this.protocolo.getNatureza() != null) {
			if (this.protocolo.getNatureza().getNome().contains("NOTIFICA")) {
				StatusProtocolo statusCertidaoEmitida = null;
				panelNotificacao.setVisible(true);
				if(this.protocolo.getIntimacaosProtocolo().size() >0) {
					for(StatusProtocolo statusProtocolo : this.protocolo.getStatusProtocolo()){
						if(statusProtocolo.getStatus().getNome().equalsIgnoreCase("CERTIDÃO INTIMAÇÃO EMITIDA")){
							statusCertidaoEmitida = statusProtocolo;
						}
					}

					if(statusCertidaoEmitida == null) {
						panelNotificacao_mensagem.setValue("Notificação em andamento");
						panelNotificacao_ate.setValue("desde");
						panelRegistrado_btnVerCertidaoIntimacao.setVisible(false);
						panelNotificacao_dtVencNotificacao.setValue(Utils.formatarDataComHora(this.protocolo.getIntimacaosProtocolo().iterator().next().getDia()));
					}else{
						panelNotificacao_mensagem.setValue("Certidão emitida");
						panelNotificacao_mensagem.removeStyleName("red");
						panelNotificacao_ate.setValue("em");
						panelRegistrado_btnVerCertidaoIntimacao.setVisible(true);
						panelNotificacao_dtVencNotificacao.setValue(Utils.formatarData(statusCertidaoEmitida.getData()));
					}

				}else {
					panelNotificacao_dtVencNotificacao.setValue(Utils.formatarData(this.protocolo.getDtRegistro().plusDays(15)));
				}
			}else{
				panelNotificacao.setVisible(false);
			}
		}

		for (StatusProtocolo statusProtocolo : this.protocolo.getStatusProtocolo()) {
			if(statusProtocolo.getStatus().getNome().equalsIgnoreCase("ENTREGUE")) {
				panelRegistrado_btnEntregue.setVisible(false);
			}
		}

	}
	public void mostrarPanelDevolvido(StatusProtocolo status, boolean icEntregue){
		//TODO COLOCAR ACOES DOS BOTOES
		panelDevolvido_dtDevolvido.setValue(Utils.formatarDataComHora(status.getData()));
		panelDevolvido.setVisible(true);
		panelDevolvido_btnEntregue.addClickListener(evt->entregarProtocolo());
		panelDevolvido_btnEntregue.setVisible(!icEntregue);
		panelDevolvido_btnReentrada.setVisible(icEntregue);
	}



	private void buscarParte(FieldEvents.BlurEvent evt) {
		TextField doc = (TextField) evt.getSource();
		if(doc.getErrorMessage() == null){
			Parte parteEncontrada = parteService.findFirstByCpfCnpjOrderByIdDesc(FormatarDocumento.formataDocumento(doc.getValue()));
			if (parteEncontrada != null) {
				//doc.setValue(FormatarDocumento.formataDocumento(doc.getValue()));
				doc.setValue(parteEncontrada.getCpfCnpj());
				parte.setValue(parteEncontrada.getNome());
			} else {
				doc.setValue(FormatarDocumento.formataDocumento(doc.getValue()));
			}
		}
	}

	private void naturezaTituloValueChangeListener(HasValue.ValueChangeEvent<Natureza> evt) {
		if(naturezaTitulo.isEnabled()) {
			Natureza selecionado = evt.getValue();
			if (selecionado != null) {
				if (cancelar == false) {
					LocalDate dtCalculo = LocalDate.now();
					if (this.protocolo.getDataProtocolo() != null) {
						dtCalculo = this.protocolo.getDataProtocolo().toLocalDate();
					}
					dataPrevista.setValue(calcularPrazoProtocolo.calcularData(selecionado.getDiasPrevisao(), true, dtCalculo));
					dataVencimento.setValue(calcularPrazoProtocolo.calcularData(selecionado.getDiasVencimento(), false, dtCalculo));
				}

				subNaturezaTitulo.clear();
				subNaturezaTitulo.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},selecionado.getSubNaturezas());
			}
		}
	}

	public void habilitarForm(boolean habilitar, boolean icProtocoloEditavel) {
		if(!icProtocoloEditavel){
			habilitar = icProtocoloEditavel;
		}
		panelAddProtocolo.setEnabled(habilitar);
		panelAddApresentante.setEnabled(habilitar);

		naturezaTitulo.focus();
	}

	public void focus(){
		naturezaTitulo.focus();
	}

	private void bindCampos() {


		binderProtocolo.bind(naturezaTitulo, "natureza");
		if(this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_TD) || this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_PJ)){
			binderProtocolo.forField(subNaturezaTitulo).bind("subNatureza");
		}else {
			binderProtocolo.forField(subNaturezaTitulo).asRequired("Favor selecionar a Sub Natureza").bind("subNatureza");
		}
        binderProtocolo.bind(ckRegistrarOutraPraca,"icRegistrarTituloOutraPraca");
		//TODO bindar quando tiver o campo no protocolo
//		binderProtocolo.bind(escrevente, "escrevente");

		binderProtocolo.forField(numeroVias).withConverter(new StringToIntegerConverter(null,"Campo deve conter apenas números")).bind(br.com.exmart.rtdpjlite.model.Protocolo::getVias, br.com.exmart.rtdpjlite.model.Protocolo::setVias);
		binderProtocolo.forField(numeroReg).withNullRepresentation("").withConverter(new StringToLongConverter(null,"Campo deve conter apenas números")).bind("numeroRegistroReferencia");
		docParte.setId("nrDocParte");
		binderProtocolo.forField(valorDocumento).withConverter(new MyStringToMoneyConverter()).bind("valor");
		binderProtocolo.forField(dtDocumento).withConverter(new MyStringToDateConverter()).bind("dataDoc");
		binderProtocolo.bind(parte, "parte");
		binderProtocolo.bind(apresentanteIdentidade, "apresentante_rg");
		binderProtocolo.forField(docParte)//.withValidator(valor -> FormatarDocumento.validarCpfCnpj(valor), "Documento Inválido")
				.bind("parteDocumento");
		binderProtocolo.bind(comboCliente,"cliente");
		binderProtocolo.bind(apresentante, "apresentante");
		binderProtocolo.bind(apresentanteTelefone, "telefone");

		binderProtocolo.bind(checkGuarda, "icGuardaConservacao");
		binderProtocolo.bind(checkSigilo, "icPossuiSigiloLegal");

		binderProtocolo.bind(apresentanteEmail, "email");

		binderProtocolo.bind(comboResponsavel, "responsavelPedido");
		binderProtocolo.bind(responsavel, "responsavel");

		binderProtocolo.bind(observacao, "observacoes");
		binderProtocolo.bind(entrega,"formaEntrega");

		binderProtocolo.bind(nrContrato,"nrContrato");

		binderProtocolo.forField(dataPrevista).bind("dataPrevista");
		binderProtocolo.forField(dataVencimento).bind("dataVencimento");
		binderProtocolo.forField(dataEntrega).withConverter(new LocalDateToLocalDateTimeConverter(ZoneId.systemDefault())).bind("dataEntrega");
		binderProtocolo.setBean(this.protocolo);

		comboCliente.addValueChangeListener(evt->comboClienteChangeListener(evt));

	}

	private void comboClienteChangeListener(HasValue.ValueChangeEvent<Cliente> evt) {
		if(evt.getComponent().isEnabled()){
			apresentante.clear();
//			responsavel.clear();
			comboResponsavel.clear();
			if(evt.getValue() != null) {
				if (!Strings.isNullOrEmpty(evt.getValue().getApresentante()))
					apresentante.setValue(evt.getValue().getApresentante());
//				if (!Strings.isNullOrEmpty(evt.getValue().getResponsavel()))
//					responsavel.setValue(evt.getValue().getResponsavel());

					comboResponsavel.setVisible(true);
					comboResponsavel.setItems(usuarioPortalRepository.findByClienteOrderByNome(evt.getValue()));
			}else{
				comboResponsavel.setVisible(false);
			}
		}
	}

	@Override
	public void validate() throws Exception {
		BinderValidationStatus<br.com.exmart.rtdpjlite.model.Protocolo> validacao = this.binderProtocolo.validate();
		if(validacao.hasErrors()){
			throw new Exception("Favor preencher corretamente todos os campos");
		}
	}

	private boolean cancelar=false;

	public void cancelar() {
		habilitarForm(false, true);
	}



	private void verificarEspecialidade() {
		if(((MyUI) UI.getCurrent()).getEspecialidadeAtual().equals(Especialidade.PJ)){
			pastaPj.setVisible(true);
			binderProtocolo.bind(pastaPj, "pastaPJ");
		}else{
			pastaPj.setVisible(false);
		}

	}

	@Override
	public void removerListener() {

	}

	@Override
	public void addListener() {

	}

	public void recalcularPrazo() {
		Natureza selecionado = this.protocolo.getNatureza();
		LocalDate dtCalculo = LocalDate.now();
		this.protocolo.setDataEntrega(null);
		dataPrevista.setValue(calcularPrazoProtocolo.calcularData(selecionado.getDiasReentrada(), true, dtCalculo));
		if(!this.protocolo.getStatusProtocolo().stream().anyMatch(status -> status.getStatus().getNome().equalsIgnoreCase("DEVOLVIDO")))
			dataVencimento.setValue(calcularPrazoProtocolo.calcularData(selecionado.getDiasReentrada(), false, dtCalculo));

		dataEntrega.setValue(null);
	}

	public void setarRetroativo(Registro registroRetroativo) {
		if(registroRetroativo != null) {
			this.protocolo.setRegistroReferencia(registroRetroativo.getRegistro());
		}else{
			this.protocolo.setRegistroReferencia(null);
			this.protocolo.setNumeroRegistroReferencia(null);
			numeroReg.clear();
		}
	}
}
