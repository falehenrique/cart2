package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.ViewPrenotacao;
import br.com.exmart.indicadorRTDPJ.ui.component.AlertaButton;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.EspecialidadeEvent;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.indicadorRTDPJ.ui.util.Especialidade;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.indicadorRTDPJ.ui.view.BuscarProtocoloView;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.repository.StatusRepository;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.rtdpjlite.util.Utils;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import pl.pdfviewer.PdfViewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ViewPrenotacaoImpl extends ViewPrenotacao implements View{

	private Registration statusRegistration;
	private ProtocoloImpl protocoloView;
	private ServicosImpl servicosView;
	private CartorioParceiroImpl cartorioParceiroView;
	private RecebimentoImpl recebimentoView;
	private PartesImpl partesView;
	private ObjetosImpl objetosView;
	private RegistroImpl registroView;
	private IntimacaoImpl intimacaoView;
	private DocumentosImpl documentosView;
	private CertidaoImpl certidaoView;
	private TipoProtocolo tipoProtocolo;
	private ArquivoService arquivoService;
	private RegistroService registroService;
	private PDFService pdfService;
	private ObjetoService objetoService;
	private CalcularPrazoProtocoloService calcularPrazoProtocoloService;

	private br.com.exmart.rtdpjlite.model.Protocolo protocolo = new br.com.exmart.rtdpjlite.model.Protocolo();
	private ProtocoloService protocoloService;
	private List<Status> statusProtocoloList;
	private Button btnRetroativo = new Button("ATUALIZAR RETROATIVO", VaadinIcons.TIMER);
	private EventBus.ViewEventBus eventBus;
	private String registroReferencia = null;
	private Long idCliente = null;
	private String formaEntrega= null;
	private Long idPedido;
	private File arquivoProtocolo = null;
	private boolean alertaIndisponivel = false;

	@Autowired
	private NavigationManager navigationManager;
	protected static Logger logger= LoggerFactory.getLogger(ViewPrenotacaoImpl.class);



	public ViewPrenotacaoImpl() {
        logger.debug("comecou ViewPrenotacaoImpl");
		this.protocoloService = BeanLocator.find(ProtocoloService.class);
		this.arquivoService = BeanLocator.find(ArquivoService.class);
		this.calcularPrazoProtocoloService = BeanLocator.find(CalcularPrazoProtocoloService.class);
		this.statusProtocoloList = ((MyUI) UI.getCurrent()).getStatus();
		this.registroService = BeanLocator.find(RegistroService.class);
		this.eventBus = BeanLocator.find(EventBus.ViewEventBus.class);
		this.objetoService = BeanLocator.find(ObjetoService.class);
		this.pdfService = BeanLocator.find(PDFService.class);
		eventBus.subscribe(this);
		status.setItemCaptionGenerator(Status::getNome);

		status.setItems(statusProtocoloList.stream().filter(item -> {return  !item.getNome().equalsIgnoreCase("REENTRADA");}).collect(Collectors.toList()));
		status.setPageLength(statusProtocoloList.size()+1);
		btnStatus.addClickListener(evt -> mostrarAndamento());

//		btnBuscar.addClickListener(evt -> mostrarBusca());


		
		btnEditar.addClickListener(evt->habilitarBotoes());
		btnSalvar.addClickListener(evt->salvarClickListener(null, ProtocoloService.ACAO.SALVAR));
		btnCancelar.addClickListener(evt->cancelarClickListener());
		conteudo.addSelectedTabChangeListener(evt->tabChangeListener(evt));

		btnVerTitulo.addClickListener(evt -> mostrarTitulo());

        logger.debug("acabou ViewPrenotacaoImpl");
		btnDuplicarProtocolo.addClickListener(evt->btnDuplicarProtocoloClickListener());
	}
		TextField txtDuplicarProtocolo = new TextField("Quantidade","0");
	private void btnDuplicarProtocoloClickListener() {
		txtDuplicarProtocolo.selectAll();
		MessageBox
			.createInfo()
			.withCaption("Replicar protocolo")
			.withMessage(txtDuplicarProtocolo)
			.withYesButton(() -> {
				try {
					Integer qtd = Integer.parseInt(txtDuplicarProtocolo.getValue());
					try {
						List<Long> protocolos = protocoloService.duplicarProtocolo(this.protocolo.getId(), qtd, ((MyUI) UI.getCurrent()).getUsuarioLogado());
						Notification.show("Sucesso","Protocolos gerados com sucesso: " + protocolos, Notification.Type.ERROR_MESSAGE);
						navigationManager.navegarPara(this.tipoProtocolo, protocolo.getNumero());
					}catch (Exception e){
						e.printStackTrace();
						Notification.show("Erro",e.getMessage(), Notification.Type.ERROR_MESSAGE);
					}
				}catch (Exception e){
					Notification.show("Erro","Favor informar um número válido!", Notification.Type.ERROR_MESSAGE);
				}
			}, ButtonOption.caption("Gerar"))
			.withCloseButton(ButtonOption.caption("Fechar"))
			.open();
	}

	private void mostrarTitulo() {
		if(splitProtocolo.getSplitPosition() >99){
			File arquivo = null;
			try {
				arquivo = arquivoService.recuperarDocumentoProtocoloFile(this.protocolo.getNumero(), ((MyUI)UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), protocolo.getTipo());
				if(arquivo != null) {
					panelPdf.removeAllComponents();
					PdfViewer pdf = new PdfViewer();
					pdf.setPreviousPageCaption("ANTERIOR");
					pdf.setNextPageCaption("PRÓXIMO");
					pdf.setPageCaption("PÁGINA ");
					pdf.setDownloadBtnVisible(true);
					pdf.setToPageCaption("DE ");
					pdf.setStyleName("pdfRTDPJ");
					pdf.setFile(arquivo);
					panelPdf.addComponentsAndExpand(pdf);

					splitProtocolo.setSplitPosition(50);
				}else{
					Notification.show("Título","Arquivo não encontrado", Notification.Type.WARNING_MESSAGE);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			splitProtocolo.setSplitPosition(100);
		}


	}

	private void mostrarBusca() {
		GerenciarJanela.abrirJanela("",90,80, new ModalBuscaImpl(),null, true, true, true, true);
	}


	private void reentradaClickListener(Button.ClickEvent evt) {
		//TODO mudar para metodo global e recuperar o click de reentrada
		GerenciarJanela.abrirJanela("REENTRADA DE TÍTULO", 65, 70, new ModalReentradaImpl(this.protocolo, statusProtocoloList.stream().filter(item -> {return  item.getNome().equalsIgnoreCase("REENTRADA");}).collect(Collectors.toList()).get(0)), null, false);
	}

	@EventBusListenerMethod(scope = EventScope.VIEW)
	public void reentradaEventListener(ProtocoloEvent.Reentrada reentrada){
		protocoloView.recalcularPrazo();
		salvarClickListener( null, ProtocoloService.ACAO.SALVAR);
	}



	@EventBusListenerMethod(scope = EventScope.VIEW)
	public void aptoRegistroEventListener(ProtocoloEvent.AptoRegistroProtocolo event){
		Status novoStatus = null;
		if(event.getProtocolo() != null){
			this.protocolo = event.getProtocolo();
			for(Status statusLoop : statusProtocoloList){
				if(statusLoop.getNome().equalsIgnoreCase("APTO PARA REGISTRO")){
					novoStatus = statusLoop;
				}
			}
			if(novoStatus != null) {
				this.protocolo.getStatusProtocolo().add(new StatusProtocolo(novoStatus,LocalDateTime.now(),  null, event.getConteudo(), ((MyUI) UI.getCurrent()).getUsuarioLogado()));
				salvarClickListener(event.getConteudo(), ProtocoloService.ACAO.REGISTRAR);

			}else{
				Notification.show("Erro", "Status APTO PARA REGISTRO não encontrado, favor entrar em contato com o Administrador", Notification.Type.ERROR_MESSAGE);
			}
		}else{
			Notification.show("Erro", "Protocolo não encontrado, favor entrar em contato com o Administrador", Notification.Type.ERROR_MESSAGE);
		}

	}


	@EventBusListenerMethod(scope = EventScope.VIEW)
	public void devolverEventListener(ProtocoloEvent.DevolverProtocolo devolver){
		salvarClickListener( devolver.getJson(), ProtocoloService.ACAO.DEVOLVER);
	}

	@EventBusListenerMethod(scope = EventScope.VIEW)
	public void registrarEventListener(ProtocoloEvent.FinalizarProtocoloCertidao finalizar){
		Status statusRegistrar = null;
		for(Status statusLoop : statusProtocoloList){
			if(statusLoop.getNome().equalsIgnoreCase("PRONTO PARA ENTREGA")){
				statusRegistrar = statusLoop;
			}
		}
		if(statusRegistrar != null) {
//			this.protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRegistrar,LocalDateTime.now(), null, finalizar.getJson(), ((MyUI) UI.getCurrent()).getUsuarioLogado()));
			salvarClickListener( finalizar.getJson(), ProtocoloService.ACAO.REGISTRAR);
			Notification.show("Finalizar", "Protocolo finalizado com sucesso", Notification.Type.HUMANIZED_MESSAGE);
			navigationManager.navigateTo(BuscarProtocoloView.class);
		}else{
			Notification.show("Erro", "Status Devolver não encontrado, favor entrar em contato com o Administrador", Notification.Type.ERROR_MESSAGE);
		}

	}



	Registro registroImpressao = null;
	@EventBusListenerMethod(scope = EventScope.VIEW)
	public void registrarEventListener(ProtocoloEvent.RegistrarProtocolo devolver){
		salvarClickListener( devolver.getJson(), ProtocoloService.ACAO.REGISTRAR);
	}

	@EventBusListenerMethod(scope = EventScope.VIEW)
	public void finalizarCertidaoIntimacao(ProtocoloEvent.FinalizarCertidaoIntimacao certidaoIntimacao){
		Status statusCertidaoIntimacao = null;
		for(Status status: statusProtocoloList){
			if(status.getNome().equalsIgnoreCase("CERTIDÃO INTIMAÇÃO EMITIDA")){
				statusCertidaoIntimacao = status;
			}
		}
		this.protocolo.getStatusProtocolo().add(new StatusProtocolo(statusCertidaoIntimacao, LocalDateTime.now(), ((MyUI) UI.getCurrent()).getUsuarioLogado()));
		salvarClickListener(certidaoIntimacao.getJson(), ProtocoloService.ACAO.SALVAR);
	}



	private void statusChangeListener(HasValue.ValueChangeEvent<Status> evt) {
		List<String> statusNaoClicados = Arrays.asList("REGISTRADO","DEVOLVIDO");
		if(statusNaoClicados.contains(evt.getValue().getNome())){
			Notification.show("Erro","O Status: " + evt.getValue().getNome() + ", não pode ser selecionado manualmente", Notification.Type.ERROR_MESSAGE);
			return;
		}
		if(this.protocolo.getStatus() != null){
			if(!this.protocolo.getStatus().equals(evt.getValue())){
				if(evt.getValue() != null) {
					if (evt.getValue().getNome().equalsIgnoreCase("ENTREGUE")) {
						GerenciarJanela.abrirJanela("ENTREGA DE TÍTULOS", 65, 70, new ModalEntregaImpl(this.protocolo, evt.getValue()), null, false);
					} else if (evt.getValue().getNome().equalsIgnoreCase("CANCEL. JUDICIALMENTE")) {
						GerenciarJanela.abrirJanela("CANCELAMENTO JUDICIAL", 65, 70, new ModalCanceladoJudImpl(this.protocolo, evt.getValue()), null, false);
					} else if (evt.getValue().getNome().equalsIgnoreCase("CANCEL. PELA PARTE")) {
						GerenciarJanela.abrirJanela("CANCELAMENTO POR SOLICITAÇÃO DA PARTE", 65, 70, new ModalCanceladoParteImpl(this.protocolo, evt.getValue()), null, false);
					} else if (evt.getValue().getNome().equalsIgnoreCase("SUSCITAÇÃO DE DÚVIDA")) {
						GerenciarJanela.abrirJanela("SUSCITAÇÃO DE DÚVIDA", 65, 70, new ModalSuscitacaoImpl(this.protocolo, evt.getValue()), null, false);
					}else{
						StatusProtocolo verificarExistente = this.protocolo.getStatusProtocolo().stream()                        // Convert to steam
								.filter(statusProtocolo ->  statusProtocolo.getStatus().getNome().equalsIgnoreCase(evt.getValue().getNome()) & statusProtocolo.getId() == null)        // we want "jack" only
								.findAny()                                      // If 'findAny' then return found
								.orElse(null);
						if(verificarExistente != null) {
							this.protocolo.getStatusProtocolo().remove(verificarExistente);
						}
						this.protocolo.getStatusProtocolo().add(new StatusProtocolo(evt.getValue(), LocalDateTime.now(), ((MyUI) UI.getCurrent()).getUsuarioLogado()));
					}
					//TODO Deve fazer acao referente ao status
					logger.error("Deve fazer acao referente ao status");
				}
			}
		}
	}

	private void tabChangeListener(TabSheet.SelectedTabChangeEvent evt) {

		for (int x = 0; x < conteudo.getComponentCount(); x++) {
			if (conteudo.getTab(x).getComponent() instanceof PassoProtocolo) {
				((PassoProtocolo) conteudo.getTab(x).getComponent()).removerListener();
			}
		}
		if(evt.getTabSheet().getSelectedTab() instanceof  PassoProtocolo) {
			((PassoProtocolo) evt.getTabSheet().getSelectedTab()).addListener();
			((PassoProtocolo) evt.getTabSheet().getSelectedTab()).focus();
		}
	}



    @EventBusListenerMethod(scope = EventScope.VIEW)
    public void salvarEventListener(ProtocoloEvent.SalvarProtocolo evt ){
	    this.protocolo = evt.getProtocolo();
	    salvarClickListener( null, ProtocoloService.ACAO.SALVAR);
    }


	private void salvarClickListener(StatusProtocoloJson statusProtocoloJson, ProtocoloService.ACAO acao) {
		try {
			for (int x = 0; x < conteudo.getComponentCount(); x++) {
				if (conteudo.getTab(x).getComponent() instanceof PassoProtocolo) {
					((PassoProtocolo) conteudo.getTab(x).getComponent()).validate();
				}
			}

			boolean novo = true;
			if (this.protocolo.getNumero() != null) {
				novo = false;
			}
			if(this.arquivoProtocolo != null){
				this.protocolo.setArquivo(FileUtils.openInputStream(arquivoProtocolo));
			}
			this.protocoloService.save(this.protocolo, acao, servicosView.getServicosCalculados(), false, false,statusProtocoloJson,((MyUI) UI.getCurrent()).getUsuarioLogado());
			if(TipoProtocolo.isPrenotacao(protocolo.getTipo()) & acao.equals(ProtocoloService.ACAO.REGISTRAR)) {
				this.registroImpressao = registroService.findByProtocolo(this.protocolo.getId());
				if(this.protocolo.getCartorioParceiroList().size() <= 0) {
					MessageBox
							.createInfo()
							.withMessage("Protocolo nº " + this.protocolo.getNumero() + " registrado sob o nº " + this.registroImpressao.getRegistro() + " com sucesso")
							.withYesButton(() -> {
								navigationManager.navegarPara(this.tipoProtocolo, protocolo.getNumero());
							}, ButtonOption.caption("Fechar"))
							.open();
				}else{
					MessageBox
							.createInfo()
							.withMessage("Protocolo nº " + this.protocolo.getNumero() + " Registrado sob o nº " + this.registroImpressao.getRegistro() + " com sucesso \n Agora as certidões serão geradas e encaminhadas automaticamente para\n os cartorios parceiros,\n FAVOR AGUARDAR...")
							.withYesButton(() -> {
								this.gerarProtocoloCertidaoAutomatico(this.registroImpressao.getNumeroRegistro(), this.registroImpressao.getRegistro(), this.protocolo);
								navigationManager.navegarPara(this.tipoProtocolo, protocolo.getNumero());
							}, ButtonOption.caption("Fechar"))
							.open();
				}
			}else{
				navigationManager.navegarPara(this.tipoProtocolo, protocolo.getNumero());
			}


		}catch (Exception e){
			Notification.show("Atenção", e.getMessage(), Notification.Type.ERROR_MESSAGE);
			e.printStackTrace();
			conteudo.setSelectedTab(0);
		}

	}

	private void gerarProtocoloCertidaoAutomatico(Long numeroRegistro, String registro, Protocolo protocoloOriginal) {
		if(BeanLocator.find(ConfiguracaoService.class).findConfiguracao().getCertidaoAutomaticaId() == null){
			Notification.show("Atenção","A natureza da certidão não foi definida, favor contatar o Administrador", Notification.Type.ERROR_MESSAGE);
		}
		Natureza naturezaProtocolo = BeanLocator.find(NaturezaService.class).findById(BeanLocator.find(ConfiguracaoService.class).findConfiguracao().getCertidaoAutomaticaId());

		if(naturezaProtocolo == null){
			Notification.show("Atenção","A natureza da certidão não foi encontrada, favor contatar o Administrador", Notification.Type.ERROR_MESSAGE);
		}else {
			TipoProtocolo tipoProtocoloCertidao = TipoProtocolo.CERTIDAO_TD;
			if (TipoProtocolo.isPj(protocoloOriginal.getTipo())) {
				tipoProtocoloCertidao = TipoProtocolo.CERTIDAO_PJ;
			}
			for (ProtocoloCartorioParceiro protocoloCartorioParceiro : protocoloOriginal.getCartorioParceiroList()) {
				logger.debug("criando certidao automaticamente");
				Protocolo novoProcolo = new Protocolo();
				novoProcolo.setTipo(tipoProtocoloCertidao);
				novoProcolo.setNatureza(naturezaProtocolo);
				novoProcolo.setIcRegistrarTituloOutraPraca(true);
				novoProcolo.setParte(protocoloOriginal.getParte());
				novoProcolo.setParteDocumento(protocoloOriginal.getParteDocumento());
				novoProcolo.setNumeroRegistroReferencia(numeroRegistro);
				novoProcolo.setRegistroReferencia(registro);
				novoProcolo.setPedido(protocoloOriginal.getPedido());
				ProtocoloCartorioParceiro novoParceiro = new ProtocoloCartorioParceiro();
				novoParceiro.setCartorioParceiro(protocoloCartorioParceiro.getCartorioParceiro());
				novoProcolo.getCartorioParceiroList().add(novoParceiro);
				novoProcolo.setCliente(protocoloOriginal.getCliente());
				novoProcolo.setApresentante(protocoloOriginal.getApresentante());
				novoProcolo.setVias(protocoloOriginal.getVias());
				novoProcolo.setValor(protocoloOriginal.getValor());
				novoProcolo.setDataDoc(protocoloOriginal.getDataDoc());
				novoProcolo.setFormaEntrega(protocoloOriginal.getFormaEntrega());
				novoProcolo.setResponsavel(protocoloOriginal.getResponsavel());
				novoProcolo.setApresentanteRg(protocoloOriginal.getApresentanteRg());
				novoProcolo.setTelefone(protocoloOriginal.getTelefone());
				novoProcolo.setEmail(protocoloOriginal.getEmail());
				novoProcolo.setObservacoes(protocoloOriginal.getObservacoes());

				try {
					this.protocoloService.save(novoProcolo, ProtocoloService.ACAO.SALVAR, new ArrayList<>(), false, false, null, ((MyUI) UI.getCurrent()).getUsuarioLogado());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void cancelarClickListener(){
		enter(this.eventCancelar);
	}

	private void mostrarAndamento() {
		GerenciarJanela.abrirJanela("ANDAMENTOS DO PROTOCOLO", 75, 75, new AndamentoImpl(this.protocolo));
	}

	private void habilitarBotoes(){
		btnSalvar.setEnabled(!btnSalvar.isEnabled());
		btnCancelar.setEnabled(!btnCancelar.isEnabled());
		btnEditar.setEnabled(!btnSalvar.isEnabled());
		btnDuplicarProtocolo.setEnabled(btnEditar.isEnabled());
		status.setEnabled(btnSalvar.isEnabled());


        for (int x = 0; x < conteudo.getComponentCount(); x++) {
            if (conteudo.getTab(x).getComponent() instanceof PassoProtocolo) {
                ((PassoProtocolo) conteudo.getTab(x).getComponent()).habilitarForm(btnSalvar.isEnabled(), protocoloService.isEditavel(protocolo));
            }
        }
		if(this.protocolo.getNumero() != null){
			status.setEnabled(btnSalvar.isEnabled());
		}else{
			status.setEnabled(false);
		}
		setarFoco();
	}

	private void setarFoco() {
		((PassoProtocolo) conteudo.getSelectedTab()).focus();
	}

	private void removerStatusListener(){
		if(statusRegistration != null){
			statusRegistration.remove();
		}
	}

	private void adicionarStatusListener(){
		removerStatusListener();
		statusRegistration = status.addValueChangeListener(evt->statusChangeListener(evt));
	}
	private ViewChangeEvent eventCancelar;


	@Override
	public void enter(ViewChangeEvent event) {
        logger.debug("comecou enter");
		this.eventCancelar = event;
		this.registroReferencia = null;
		this.idCliente = null;
		this.formaEntrega = null;
		this.idPedido = null;
		this.arquivoProtocolo = null;
		removerStatusListener();
		atualizarEspecialidade(event.getViewName());

		if(event.getViewName().toUpperCase().contains("PROTOCOLO")){
			if(((MyUI) UI.getCurrent()).getEspecialidadeAtual().equals(Especialidade.PJ)){
				this.tipoProtocolo = TipoProtocolo.PRENOTACAO_PJ;
			}else{
				this.tipoProtocolo = TipoProtocolo.PRENOTACAO_TD;
			}
		}else if(event.getViewName().toUpperCase().contains("EXAME")){
			if(((MyUI) UI.getCurrent()).getEspecialidadeAtual().equals(Especialidade.PJ)){
				this.tipoProtocolo = TipoProtocolo.EXAMECALCULO_PJ;
			}else{
				this.tipoProtocolo = TipoProtocolo.EXAMECALCULO_TD;
			}
		}else if(event.getViewName().toUpperCase().contains("CERTIDAO")){
			if(((MyUI) UI.getCurrent()).getEspecialidadeAtual().equals(Especialidade.PJ)){
				this.tipoProtocolo = TipoProtocolo.CERTIDAO_PJ;
			}else{
				this.tipoProtocolo = TipoProtocolo.CERTIDAO_TD;
			}
		}
		Long id = null;


		if(!Strings.isNullOrEmpty(event.getParameters())){
			String[] parametros = event.getParameters().split("/?");
			try {
				id = Long.parseLong(navigationManager.recuperarValorParametro("numeroProtocolo", event.getParameters()));
			}catch (Exception e){
			}
			registroReferencia = navigationManager.recuperarValorParametro("registroReferencia", event.getParameters());
			try {
				idCliente = Long.parseLong(navigationManager.recuperarValorParametro("idCliente", event.getParameters()));
			}catch (Exception e){
			}
			try {
				idPedido = Long.parseLong(navigationManager.recuperarValorParametro("idPedido", event.getParameters()));
			}catch (Exception e){
			}
			try{
				String nomeArquivo = navigationManager.recuperarValorParametro("filename", event.getParameters());
				if(!Strings.isNullOrEmpty(nomeArquivo)){
					File temp = File.createTempFile("temp_file",".tmp");
					String absolutePath = temp.getAbsolutePath();
					String tempFilePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
					arquivoProtocolo = new File(tempFilePath+File.separator+nomeArquivo);
				}
			}catch (Exception e){
				e.printStackTrace();

			}
			this.formaEntrega = navigationManager.recuperarValorParametro("formaEntrega",event.getParameters());
		}
		atualizarDadosProtocolo(id);
		if(this.protocolo != null) {
			verificaStatus();
			adicionarStatusListener();
		}
        logger.debug("acabou enter");
	}

	private void verificaStatus() {
        logger.debug("comecou verificaStatus");

		//TODO deve melhorar esse controle
		if(this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_TD) || this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_PJ)){
			boolean icFinalizado = false;
			if(this.protocolo.getStatus() != null) {
				for (StatusProtocolo statusProtocolo : this.protocolo.getStatusProtocolo()) {
					if (statusProtocolo.getStatus().getNome().contains("PRONTO PARA ENTREGA")) {
						icFinalizado = true;
					}
				}
			}
			if(icFinalizado){
				panelEditarExame.setVisible(false);
			}else{
				panelEditarExame.setVisible(true);
			}
		}else{
			boolean icRegistrado = false;
			if(this.protocolo.getStatus() != null) {
				for (StatusProtocolo statusProtocolo : this.protocolo.getStatusProtocolo()) {
					if (statusProtocolo.getStatus().getNome().contains("REGISTRADO")) {
						icRegistrado = true;
					}
				}
			}
			if(this.protocolo.getStatus() != null && (this.protocolo.getStatus().getStatus().getNome().equalsIgnoreCase("ENTREGUE") & !icRegistrado)){
				panelEditarExame.setVisible(false);
			}else{
				panelEditarExame.setVisible(true);

			}
		}
        logger.debug("acabou verificaStatus");
	}

	private void atualizarEspecialidade(String viewName) {
		if(viewName.contains("pj")){
			eventBus.publish(this, new EspecialidadeEvent(Especialidade.PJ));
		}else{
			eventBus.publish(this, new EspecialidadeEvent(Especialidade.TD));
		}

	}

	private void atualizarDadosProtocolo(Long idProtocolo) {
        logger.debug("comecou atualizarDadosProtocolo: ", idProtocolo);
		btnSalvar.setEnabled(true);
		btnCancelar.setEnabled(true);
		btnEditar.setEnabled(false);
		conteudo.removeAllComponents();

		if(idProtocolo ==null) {
			status.setEnabled(false);
			this.protocolo = new br.com.exmart.rtdpjlite.model.Protocolo();
			this.protocolo.setTipo(this.tipoProtocolo);
		}else{
			this.protocolo = BeanLocator.find(ProtocoloService.class).findByNumeroAndTipoProtocolo(idProtocolo, this.tipoProtocolo);
			if(this.protocolo == null){
				Notification.show("Protocolo não encontrado", Notification.Type.ERROR_MESSAGE);
				navigationManager.navigateTo(BalcaoImpl.class);
			}
		}


		boolean icCertidao = false;
		boolean icMostrarAbaTitulo = true;
		if(this.protocolo != null){
			protocoloView = new ProtocoloImpl(this.protocolo, this.eventBus, this.registroReferencia, this.idCliente, this.formaEntrega,this.idPedido);
			conteudo.addTab(protocoloView, "Protocolo", VaadinIcons.HASH);
			servicosView = new ServicosImpl(this.protocolo, this.eventBus);
			cartorioParceiroView = new CartorioParceiroImpl(this.protocolo);
			conteudo.addTab(servicosView , "Serviços", VaadinIcons.FILE_ADD);
			if(this.tipoProtocolo.equals(TipoProtocolo.PRENOTACAO_PJ) || this.tipoProtocolo.equals(TipoProtocolo.PRENOTACAO_TD)) {
				labelTituloProtocolo.setValue("PRENOTAÇÃO - " + ((MyUI) UI.getCurrent()).getEspecialidadeAtual().getEspecialidade());
//				recebimentoView = new RecebimentoImpl(this.protocolo);
				conteudo.addTab(recebimentoView, "Financeiro", VaadinIcons.MONEY);
			}else if(this.tipoProtocolo.equals(TipoProtocolo.EXAMECALCULO_TD) || this.tipoProtocolo.equals(TipoProtocolo.EXAMECALCULO_PJ)) {
				labelTituloProtocolo.setValue("EXAME CÁLCULO - " + ((MyUI) UI.getCurrent()).getEspecialidadeAtual().getEspecialidade());
			}else if(this.tipoProtocolo.equals(TipoProtocolo.CERTIDAO_PJ) || this.tipoProtocolo.equals(TipoProtocolo.CERTIDAO_TD)) {
				icCertidao=true;
				labelTituloProtocolo.setValue("CERTIDÃO - " + ((MyUI) UI.getCurrent()).getEspecialidadeAtual().getEspecialidade());
//				recebimentoView = new RecebimentoImpl(this.protocolo);
				conteudo.addTab(recebimentoView, "Financeiro", VaadinIcons.MONEY);
				if(protocolo.getNatureza() != null){
					if(protocolo.getNatureza().getTipoEmissaoCertidao() == TipoEmissaoCertidao.IMAGEM_INTEIRO_TEOR){
						icMostrarAbaTitulo= false;
						if(!Strings.isNullOrEmpty(protocolo.getRegistroReferencia())){
							VerticalLayout panelTimeline = new VerticalLayout();
							VerticalLayout panelBotoes = new VerticalLayout();
                            panelBotoes.addComponent(this.btnRetroativo);
//							panelTimeline.setSizeFull();
							panelTimeline.setStyleName("scroll marginLarge well");
							panelTimeline.addComponent(panelBotoes);

                            this.btnRetroativo.setStyleName("primary");
							logger.debug("iniciou montartimeline");
							for(CardTimeline card : this.protocoloService.getTimeLine(this.protocolo.getRegistroReferencia(), TipoProtocolo.recuperaEspecialidade(this.protocolo.getTipo()))){
								if(card != null) {
									panelTimeline.addComponent(new CardLinhaTempoImpl(card, null));
								}
							}
							logger.debug("acabou montartimeline");
							conteudo.addTab(panelTimeline, "Inteiro Teor", VaadinIcons.FOLDER_OPEN_O);
						}
					}
				}
			}


			if(icMostrarAbaTitulo) {
				if(this.protocolo.getNumero() != null) {
					documentosView = new DocumentosImpl(this.protocolo);
					conteudo.addTab(documentosView, "Título", VaadinIcons.FOLDER_OPEN_O);
				}
			}



			if(this.protocolo.getNumero() == null){
				labelNumeroProtocolo.setValue(" - ");
				labelDataProtocolo.setValue( " - ");
			}else{
				labelNumeroProtocolo.setValue(this.protocolo.getNumero().toString());
				labelDataProtocolo.setValue(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(this.protocolo.getDataProtocolo()));
				//TODO Verificar quando deve ser adicionado
				if(icCertidao){
//					btnVerTitulo.setVisible(false);
					certidaoView = new CertidaoImpl(this.protocolo, this.eventBus);
                    conteudo.addTab(cartorioParceiroView, "Parceiros", VaadinIcons.USERS);
					conteudo.addTab(certidaoView, "Certidão", VaadinIcons.FILE_PICTURE);
					servicosView.disparaEventBus();
				}else{
//					btnVerTitulo.setVisible(true);

					if(!this.tipoProtocolo.equals(TipoProtocolo.EXAMECALCULO_TD) && !this.tipoProtocolo.equals(TipoProtocolo.EXAMECALCULO_PJ)) {

						partesView = new PartesImpl(this.protocolo, this.eventBus);
						objetosView = new ObjetosImpl(this.protocolo, objetoService, this.eventBus);

						conteudo.addTab(partesView, "Partes", VaadinIcons.USERS);
						if(((MyUI) UI.getCurrent()).getEspecialidadeAtual().equals(Especialidade.TD)) {
							conteudo.addTab(objetosView, "Objetos", VaadinIcons.CAR);
						}
					}

					if(!this.tipoProtocolo.equals(TipoProtocolo.CERTIDAO_PJ) || !this.tipoProtocolo.equals(TipoProtocolo.CERTIDAO_TD)) {
						registroView = new RegistroImpl(this.protocolo, this.eventBus);
						conteudo.addTab(cartorioParceiroView, "Parceiros", VaadinIcons.USERS);
						conteudo.addTab(registroView, "Análise", VaadinIcons.FILE_PROCESS);

						if(this.protocolo.getNatureza() != null){
							if(this.protocolo.getNatureza().getNome().toUpperCase().startsWith("NOTIFICA")) {
								if (this.protocolo.isRegistrado()){
									intimacaoView = new IntimacaoImpl(this.protocolo);
									conteudo.addTab(intimacaoView, "Intimação", VaadinIcons.ENVELOPE_OPEN_O);
								}
							}
						}
					}

				}
				habilitarBotoes();
				status.setValue(this.protocolo.getStatus().getStatus());
			}

			setarFoco();
		}
		montarMenuImpressao();
        logger.debug("acabou atualizarDadosProtocolo");
	}

	Button.ClickListener clickListener;

	private void montarMenuImpressao(){

		menuImpressao.removeItems();
		if(this.protocolo != null) {
			if (this.protocolo.getNumero() == null) {
				menuImpressao.setVisible(false);
				return;
			}
			EnhancedBrowserWindowOpener protocoloOpener = new EnhancedBrowserWindowOpener();

			MenuBar.MenuItem impresso = menuImpressao.addItem("Imprimir Protocolo", evt -> protocoloOpener.open("/protocolo/" + this.protocolo.getTipo().getId() + "/"+ this.protocolo.getNumero() + "/imprimir"));
			MenuBar.MenuItem subMenu = menuImpressao.addItem("", null);
			MenuBar.MenuItem imprimirProtocolo = subMenu.addItem("Imprimir Protocolo", evt -> protocoloOpener.open("/protocolo/"  + this.protocolo.getTipo().getId() + "/" + this.protocolo.getNumero() + "/imprimir"));

			if (this.protocolo.getDtRegistro() != null) {
				Registro registro = registroService.findByProtocoloIdAndEspecialidade(protocolo.getId(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()));
				MenuBar.MenuItem imprimirCapa = subMenu.addItem("Imprimir Capa", evt -> protocoloOpener.open("/registro/" + this.protocolo.getTipo().getId() + "/" + registro.getRegistro() + "/imprimir/capa"));
				MenuBar.MenuItem imprimirRecibo = subMenu.addItem("Imprimir Recibo", evt -> protocoloOpener.open("/registro/" + this.protocolo.getTipo().getId() + "/" +  this.protocolo.getNumero() + "/imprimir/recibo"));
			}



			if(this.protocolo.getTipo().equals(TipoProtocolo.PRENOTACAO_TD) ||  this.protocolo.getTipo().equals(TipoProtocolo.PRENOTACAO_PJ)){
				if(!this.protocolo.getFormaEntrega().getNome().equalsIgnoreCase("DIGITAL")){
					if(!Strings.isNullOrEmpty(this.protocolo.getNumeroRegistro())) {
						MenuBar.MenuItem imprimirCarimbo = subMenu.addItem("Imprimir Carimbo", evt -> {
							try {
								protocoloOpener.open(generateResource(registroService.findByProtocolo(this.protocolo.getId())));
							} catch (Exception e) {
								Notification.show("Atenção", "Documento do registro não encontrado", Notification.Type.ERROR_MESSAGE);
							}
						});
					}
				}
			}

			List<StatusProtocolo> listaStatus = this.protocolo.getStatusProtocolo().stream().sorted(Comparator.comparing(d -> d.getData())).collect(Collectors.toList());
			for (StatusProtocolo statusProtocolo : listaStatus) {
				if (statusProtocolo.getStatus().getNome().equalsIgnoreCase("DEVOLVIDO")) {
					EnhancedBrowserWindowOpener devolucaoOpener = new EnhancedBrowserWindowOpener();
					MenuBar.MenuItem devolucaoMenuItem = subMenu.addItem("Nota de Devolução ("+Utils.formatarDataComHora(statusProtocolo.getData()) +")", evt -> devolucaoOpener.open("/protocolo/" + this.protocolo.getTipo().getId()+ "/"+this.protocolo.getNumero() + "/devolucao/"+statusProtocolo.getId()));
					devolucaoOpener.doExtend(menuImpressao, devolucaoMenuItem);
				}
			}
			if(this.protocolo.getNatureza() != null){
				if(this.protocolo.getNatureza().getNome().equalsIgnoreCase("NOTIFICAÇÃO")){
					subMenu.addItem("Solicitação de Comparecimento", evt -> protocoloOpener.open("/protocolo/" + this.protocolo.getTipo().getId() + "/"+ this.protocolo.getNumero() + "/comparecimento"));
					subMenu.addItem("Checklist de Intimação", evt -> protocoloOpener.open("/protocolo/" + this.protocolo.getTipo().getId() + "/"+ this.protocolo.getNumero() + "/checklistintimacao"));
				}
			}
			if (!subMenu.hasChildren()) {
				menuImpressao.removeItem(subMenu);
			}
			protocoloOpener.doExtend(menuImpressao, impresso);
			protocoloOpener.doExtend(menuImpressao, imprimirProtocolo);

//		impresso.setCommand(menuItem -> EnhancedBrowserWindowOpener.extendOnce(impresso).open("/protocolo/"+this.protocolo.getNumero()+"/imprimir"));

			//		btnImprimir.addClickListener(evt -> EnhancedBrowserWindowOpener.extendOnce(btnImprimir).open("/protocolo/"+this.protocolo.getNumero()+"/imprimir"));
		}
		}

	private StreamResource generateResource(Registro registro) throws Exception {

        StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                try {
                    return new FileInputStream(pdfService.exportarPagina(arquivoService.recuperarDocumentoProtocoloRegistroFile(registro.getRegistro(), registro.getNumeroRegistro(),registro.getEspecialidade(), null), PDFService.DisposicaoPagina.ULTIMA_PAGINA));
                } catch (IOException e) {
                    return null;
                }
            }
        };

        if(streamSource == null){
        	throw new Exception("Arquivo não encontrado");
		}

			StreamResource resource = new StreamResource(streamSource,registro.getRegistro()+".PDF");
			resource.setMIMEType("application/pdf");
			resource.getStream().setParameter(
					"Content-Disposition",
					"attachment; filename="+registro.getRegistro()+".PDF");


			return resource;
	}

	AlertaButton alertaIndisponibilidade = new AlertaButton(AlertaButton.Tipos.INDISPONIBILIDADE);
    @EventBusListenerMethod(scope = EventScope.VIEW)
    public void protocolarExameCalculoEventListener(ProtocoloEvent.ParteIndisponivel event){
		panelAlertas.setVisible(true);
//		panelAlertas.addComponent(new AlertaButton(AlertaButton.Tipos.CONTRADITORIO));
			if(event.isPossui()) {
				if(panelAlertas.getComponentIndex(alertaIndisponibilidade)<=0) {
					panelAlertas.addComponent(alertaIndisponibilidade);
				}
			}else{
				panelAlertas.removeComponent(alertaIndisponibilidade);
			}
//		panelAlertas.addComponent(new AlertaButton(AlertaButton.Tipos.IMAGEM_INEXISTENTE));
    }

	@EventBusListenerMethod(scope = EventScope.VIEW)
	public void protocolarExameCalculoEventListener(ProtocoloEvent.ProtocolarExameCalculo event){
		try {
			Long idExameCalculo = event.getProtocolo().getNumero();
			TipoProtocolo tipoAntigo = this.protocolo.getTipo();
			Long numeroAntigo = this.protocolo.getNumero();
			if(event.getProtocolo().getTipo().equals(TipoProtocolo.EXAMECALCULO_PJ)){
				this.protocolo = event.getProtocolo().clone(TipoProtocolo.PRENOTACAO_PJ, this.calcularPrazoProtocoloService);
			}else{
				this.protocolo = event.getProtocolo().clone(TipoProtocolo.PRENOTACAO_TD, this.calcularPrazoProtocoloService);
			}
			this.protocolo.setArquivo(arquivoService.recuperarDocumentoProtocoloIS(numeroAntigo,TipoProtocolo.recuperaEspecialidade(tipoAntigo), tipoAntigo));
			this.protocolo.setNrExameCalculoReferencia(idExameCalculo);
			this.tipoProtocolo = this.protocolo.getTipo();
			servicosView.prepararPrenotarExameCalculo();
			salvarClickListener( null, ProtocoloService.ACAO.SALVAR);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void beforeLeave(ViewBeforeLeaveEvent event) {
		protocoloView = null;
		servicosView = null;
		System.gc();
		View.super.beforeLeave(event);

	}
}
