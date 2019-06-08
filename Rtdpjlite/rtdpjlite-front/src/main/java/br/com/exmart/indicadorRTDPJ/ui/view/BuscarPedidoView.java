package br.com.exmart.indicadorRTDPJ.ui.view;

import br.com.exmart.indicadorRTDPJ.ui.impl.BuscarPedidosImpl;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.rtdpjlite.model.FormaEntrega;
import br.com.exmart.rtdpjlite.service.ArquivoService;
import br.com.exmart.rtdpjlite.service.ClienteService;
import br.com.exmart.rtdpjlite.service.FormaEntregaService;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;
import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

@SpringView(name = "pedidos")
@UIScope
@MenuCaption("BALCÃO ONLINE")
@MenuIcon(VaadinIcons.INBOX)
public class BuscarPedidoView extends VerticalLayout implements View {
    @Autowired
    private EventBus.ViewEventBus eventBus;
    @Autowired
    private NavigationManager navigationManager;


    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private FormaEntregaService formaEntrega;
    @Autowired
    private ProtocoloService protocoloService;
    @Autowired
    private ArquivoService arquivoService;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        removeAllComponents();
        addComponentsAndExpand(new BuscarPedidosImpl(pedidoService, eventBus, clienteService, formaEntrega, navigationManager, protocoloService, arquivoService));
    }
}
