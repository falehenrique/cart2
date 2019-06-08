package br.com.exmart.indicadorRTDPJ.ui.view;

import br.com.exmart.indicadorRTDPJ.ui.impl.BuscarProtocolosImpl;
import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

@SpringView(name = "protocolos")
@UIScope
@MenuCaption("PROTOCOLOS")
@MenuIcon(VaadinIcons.LIST_OL)
public class BuscarProtocoloView extends BuscarProtocolosImpl implements View {

}
