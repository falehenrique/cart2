package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.MainView;
import br.com.exmart.indicadorRTDPJ.ui.PrincipalSliderMenu;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.EspecialidadeEvent;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.indicadorRTDPJ.ui.util.Especialidade;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderTabPosition;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import javax.annotation.PostConstruct;


@ViewScope
@SpringView(name = "sliderMenu")
public class PrincipalSliderMenuImpl extends PrincipalSliderMenu implements View{

    public PrincipalSliderMenuImpl() {

        SliderPanel sliderMenu = new SliderPanelBuilder(genFilter())
                .expanded(false)
                .mode(SliderMode.LEFT)
                .caption("Menu")
                .tabPosition(SliderTabPosition.BEGINNING)
                .build();

        addComponent(sliderMenu);
        content.addComponent(new ViewPrenotacaoImpl());
    }

    private Component genFilter() {
        Button inicio = new Button("INÍCIO", VaadinIcons.DASHBOARD);
        return inicio;
    }
}
