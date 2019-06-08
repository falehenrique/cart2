package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.Balcao;
import br.com.exmart.indicadorRTDPJ.ui.component.SuggestionProviderPJ;
import br.com.exmart.indicadorRTDPJ.ui.component.SuggestionProviderTD;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.EspecialidadeEvent;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.indicadorRTDPJ.ui.util.Especialidade;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.FormaEntrega;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;
import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.vaadin.spring.events.EventBus;

import javax.annotation.PostConstruct;
import javax.management.Notification;
import java.util.stream.Collectors;

@SpringView(name = "")
@UIScope
@MenuCaption("Recepção")
@MenuIcon(VaadinIcons.SHOP)
public class BalcaoImpl extends Balcao implements View{

	private EventBus.ViewEventBus eventBus;
	private NavigationManager navigationManager;


	private PedidoService pedidoService;
	private ClienteService clienteService;
	private FormaEntregaService formaEntrega;
	private ProtocoloService protocoloService;
	private ArquivoService arquivoService;

	public static final String VIEW_NAME = "recepcao";
	private PrincipalRTDPJImpl principalRTDPJ;
	SuggestionProviderPJ suggestionProviderPJ;
	SuggestionProviderTD suggestionProviderTD;

	@Autowired
	public BalcaoImpl(
					EventBus.ViewEventBus eventBus,
					NavigationManager navigationManager,
					PedidoService pedidoService,
					ClienteService clienteService,
					FormaEntregaService formaEntrega,
					ProtocoloService protocoloService,
					ArquivoService arquivoService,
					SigningService signingService
	) {

//		vlBalcaoOnline.addComponentsAndExpand(new BuscarPedidosImpl(pedidoService, eventBus, clienteService, formaEntrega, navigationManager, protocoloService, arquivoService));

		btnNovaPrenotacaoTD.addClickListener(evt -> {
			eventBus.publish(this, new EspecialidadeEvent(Especialidade.TD));
			navigationManager.navegarPara(TipoProtocolo.PRENOTACAO_TD);});
		btnNovaCertidaoTD.addClickListener(evt -> {
			eventBus.publish(this, new EspecialidadeEvent(Especialidade.TD));
			navigationManager.navegarPara(TipoProtocolo.CERTIDAO_TD);});
		btnNovoCalculoTD.addClickListener(evt -> {
			eventBus.publish(this, new EspecialidadeEvent(Especialidade.TD));
			navigationManager.navegarPara(TipoProtocolo.EXAMECALCULO_TD);});
		btnNovaPrenotacaoPJ.addClickListener(evt -> {
			eventBus.publish(this, new EspecialidadeEvent(Especialidade.PJ));
			navigationManager.navegarPara(TipoProtocolo.PRENOTACAO_PJ);});
		btnNovaCertidaoPJ.addClickListener(evt -> {
			eventBus.publish(this, new EspecialidadeEvent(Especialidade.PJ));
			navigationManager.navegarPara(TipoProtocolo.CERTIDAO_PJ);});
		btnNovoCalculoPJ.addClickListener(evt -> {
			eventBus.publish(this, new EspecialidadeEvent(Especialidade.PJ));
			navigationManager.navegarPara(TipoProtocolo.EXAMECALCULO_PJ);});

		btnRegistroTDLote.addClickListener(evt -> abrirLote(TipoProtocolo.PRENOTACAO_TD));

		this.clienteService = clienteService;
		this.protocoloService = protocoloService;

//		equal-width-tabs framed padded-tabbar


		try {
			Long diasExpirar = signingService.diasExpirar();
			System.out.println(diasExpirar);
			if(diasExpirar < 15)
				com.vaadin.ui.Notification.show("Atenção", "Certificado Digital expira em " + diasExpirar + " dias", com.vaadin.ui.Notification.Type.ERROR_MESSAGE);
		} catch (Exception e) {
			com.vaadin.ui.Notification.show("Atenção", "Certificado Digital: " + 			e.getMessage(), com.vaadin.ui.Notification.Type.ERROR_MESSAGE);
			e.printStackTrace();

		}
	}

	private void abrirLote(TipoProtocolo tipoProtocolo) {
		GerenciarJanela.abrirJanela(null, 45, 50, new ModalProtocolarLoteImpl(tipoProtocolo), null, true);
	}

	@Override
	public void enter(ViewChangeEvent event) {

//		Cliente cliente = clienteService.findAll().get(0);
//		protocoloService.findByClientePageable(cliente, new PageRequest(2,30));
	}

}
