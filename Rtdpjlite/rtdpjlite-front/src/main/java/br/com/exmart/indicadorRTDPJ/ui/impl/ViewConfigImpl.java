package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.ViewConfig;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;

@SpringView (name = "config")
@UIScope
public class ViewConfigImpl extends ViewConfig implements View{
    public ViewConfigImpl() {

    }
}
