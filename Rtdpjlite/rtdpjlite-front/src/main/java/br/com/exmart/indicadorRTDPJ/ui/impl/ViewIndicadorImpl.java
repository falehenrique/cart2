package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.ViewIndicador;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;

@SpringView(name = "detalhes-indicador")
@UIScope
public class ViewIndicadorImpl extends ViewIndicador implements View{

    public ViewIndicadorImpl() {
        panelTimeline.addComponent(new CardRegistroTimelineImpl());
    }
}
