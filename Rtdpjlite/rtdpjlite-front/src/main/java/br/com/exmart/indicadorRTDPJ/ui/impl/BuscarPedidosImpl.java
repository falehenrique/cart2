package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.BuscarPedidos;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.indicadorRTDPJ.ui.view.ModalVisualizarDocumento;
import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.FormaEntrega;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.model.portal.Pedido;
import br.com.exmart.rtdpjlite.service.ArquivoService;
import br.com.exmart.rtdpjlite.service.ClienteService;
import br.com.exmart.rtdpjlite.service.FormaEntregaService;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;
import com.google.common.base.Strings;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.HtmlRenderer;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BuscarPedidosImpl extends BuscarPedidos {

	private PedidoService pedidoService;
	private ClienteService clienteService;
	private FormaEntrega formaEntrega;
	private NavigationManager navigationManager;
	private EventBus.ViewEventBus eventBus;

	private ProtocoloService protocoloService;
	private ArquivoService arquivoService;
	private List<Pedido> pedidoOriginalList;
    private List<Pedido> pedidoFiltrado = new ArrayList<>();
	protected static Logger logger = LoggerFactory.getLogger(BuscarPedidosImpl.class);

	
	public BuscarPedidosImpl(PedidoService pedidoService, EventBus.ViewEventBus eventBus, ClienteService clienteService, FormaEntregaService formaEntrega, NavigationManager navigationManager, ProtocoloService protocoloService, ArquivoService arquivoService
	){
		btnFiltrarData.addClickListener(evt->mostrarFiltro());
		this.clienteService = clienteService;
		this.pedidoService = pedidoService;
		this.protocoloService = protocoloService;
		this.formaEntrega = formaEntrega.findByNome("Digital");
		this.arquivoService = arquivoService;
		this.navigationManager = navigationManager;
		this.eventBus = eventBus;
		gridBusca.setSelectionMode(Grid.SelectionMode.MULTI);
		btnPrenotarTD.addClickListener(evt->btnPrenotarTDClickListener());
		btnPrenotarPJ.addClickListener(evt->btnPrenotarPJClickListener());

//		comboPeditoMensagemTipo.setItems(PedidoMensagemTipo.listar());

		this.eventBus.subscribe(this);
		arrumarGrid();
		atualizarPedidos();
	}

	private void atualizarPedidos(){
		this.gridBusca.setItems(pedidoService.findNovos());
	}

//    private void comboPeditoMensagemTipoListener(HasValue.ValueChangeEvent<PedidoMensagemTipo> evt) {
//        this.pedidoFiltrado.clear();
//        this.pedidoFiltrado.addAll(this.pedidoOriginalList.stream().filter(pedido -> {
//            if(evt.getValue() == null){
//                return true;
//            }else {
//            	if(pedido.getUltimoStatus() == null){
//            		return false;
//				}
//                return pedido.getUltimoStatus().equalsIgnoreCase(evt.getValue().getNome());
//            }
//        }).collect(Collectors.toList()));
//        this.gridBusca.getDataProvider().refreshAll();
//    }

    private void arrumarGrid() {
		gridBusca.removeAllColumns();
		gridBusca.addColumn(Pedido::getId).setCaption("Nº Pedido").setWidth(110);
		gridBusca.addColumn(item -> {return Utils.formatarDataComHora(item.getData());}, new HtmlRenderer()).setCaption("Data").setWidth(180);
        gridBusca.addColumn(Pedido::getCliente).setCaption("Cliente");
		gridBusca.addColumn(Pedido::getResponsavel).setCaption("Apresentante").setMinimumWidthFromContent(true);
		gridBusca.addComponentColumn(pedido -> {
			Button button = new Button(VaadinIcons.FILE_SEARCH);
			button.addStyleName("borderless");
			button.addClickListener(click -> {
				try {
					GerenciarJanela.abrirJanela("Documento do Pedido", 90, 60, new ModalVisualizarDocumento(recuperarArquivoPedidoFile(pedido.getId())));
				}catch (Exception e ){
					Notification.show("Atenção", "Não foi possível recuperar o arquivo do pedido: "+ pedido.getId(), Notification.Type.ERROR_MESSAGE);
				}
			});
			return button;
		}).setWidth(90).setCaption("Arquivo");

	}

	private void btnPrenotarTDClickListener() {
		MessageBox
			.createQuestion()
			.withCaption("Atenção")
			.withMessage("Deseja protocolar os pedidos selecionados como TD?.")
			.withYesButton(() -> {
				GerenciarJanela.abrirJanela("Protocolar Pedido",50, 50, new ModalProtocoloBasicoImpl(this.eventBus, TipoProtocolo.PRENOTACAO_TD));

			}, ButtonOption.caption("SIM"), ButtonOption.focus())
			.withNoButton(ButtonOption.caption("NÃO"))
			.open();
	}

	@EventBusListenerMethod(scope = EventScope.VIEW)
	private void prenotarPedidos(ProtocoloEvent.ProtocolarLote protocolarEvent) {
		try {
			List<Protocolo> protocolos = new ArrayList<>();
			for(br.com.exmart.rtdpjlite.model.portal.Pedido pedido : gridBusca.getSelectedItems()){
				if(pedido.getProtocolo() == null){
					Protocolo novoProtocolo = new Protocolo();
					novoProtocolo.setTipo(protocolarEvent.getTipoProtocolo());
					novoProtocolo.setNatureza(protocolarEvent.getNatureza());
					novoProtocolo.setSubNatureza(protocolarEvent.getSubNatureza());
					novoProtocolo.setApresentante(pedido.getResponsavel().getNome());
					novoProtocolo.setEmail(pedido.getResponsavel().getEmail());
					novoProtocolo.setData_doc(LocalDate.now());
					novoProtocolo.setParteDocumento(pedido.getParteDocumento());
					novoProtocolo.setParte(pedido.getParte());
					novoProtocolo.setFormaEntrega(this.formaEntrega);
					novoProtocolo.setPedido(pedido.getId());
					novoProtocolo.setVias(1);
					novoProtocolo.setObservacoes(pedido.getObservacao());
					novoProtocolo.setCliente(pedido.getCliente());
					novoProtocolo.setResponsavelPedido(pedido.getResponsavel());
					novoProtocolo.setArquivo(recuperarArquivoPedido(pedido.getId()));
					protocolos.add(novoProtocolo);


				}
			}
			for(Protocolo protocolo : protocolos){
				protocoloService.save(protocolo, ProtocoloService.ACAO.SALVAR, new ArrayList<>(), true, false, null,((MyUI) UI.getCurrent()).getUsuarioLogado());
			}
			atualizarPedidos();
		}catch (Exception e){
			e.printStackTrace();
			Notification.show("Atenção", e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

	private File recuperarArquivoPedidoFile(Long idPedido) throws Exception{
		File retorno = null;
		try {
			retorno = arquivoService.recuperarArquivoPedidoFile(idPedido);
		}catch (Exception fn){

		}
		if(!retorno.exists()){
			File tempFile = null;
				tempFile = File.createTempFile(idPedido+"_temp",".PDF",null);
				FileOutputStream fos = new FileOutputStream(tempFile);
				//FIXME corrigir
//				fos.write(pedidoService.recuperarArquivoPedido(idPedido));
				retorno = tempFile;
		}
		return  retorno;
	}
	private InputStream recuperarArquivoPedido(Long idPedido){
		try {
			return arquivoService.recuperarArquivoPedido(idPedido);
		}catch (IOException e){
			//FIXME corrigir
//			return new ByteArrayInputStream(pedidoService.recuperarArquivoPedido(idPedido));
			return null;
		}
	}

	private void btnPrenotarPJClickListener() {
		MessageBox
			.createQuestion()
			.withCaption("Atenção")
			.withMessage("Deseja protocolar os pedidos selecionados como PJ?.")
			.withYesButton(() -> {
				GerenciarJanela.abrirJanela("Protocolar Pedido",50, 50, new ModalProtocoloBasicoImpl(this.eventBus, TipoProtocolo.PRENOTACAO_PJ));
			}, ButtonOption.caption("SIM"), ButtonOption.focus())
			.withNoButton(ButtonOption.caption("NÃO"))
			.open();
	}

	private void mostrarFiltro() {
		filtroData.setVisible(!filtroData.isVisible());
    }
//
//    public void enter() {
//		try{
//			//FIXME listar os pedidos
////            pedidoOriginalList= pedidoService.listarPedidos();
//            this.pedidoFiltrado.addAll(pedidoOriginalList);
//        	gridBusca.setItems(this.pedidoFiltrado);
//		}catch (HttpClientErrorException e){
//			logger.error("Erro ao buscar pedidos {}", e.getMessage());
////			Notification.show("Atenção", "Nenhum pedido encontrado", Notification.Type.ERROR_MESSAGE);
//		}catch (Exception e){
//			logger.error("Erro ao buscar pedidos {}", e.getMessage());
//			Notification.show("Atenção", "Erro ao acessar o portal para listar os pedidos", Notification.Type.ERROR_MESSAGE);
//		}
//    }
}
