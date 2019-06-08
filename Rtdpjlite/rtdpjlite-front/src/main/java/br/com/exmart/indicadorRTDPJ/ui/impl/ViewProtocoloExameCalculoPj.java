package br.com.exmart.indicadorRTDPJ.ui.impl;

import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;

@ViewScope
@SpringView(name = "exame-pj")
@MenuCaption("Exame e Cálculo PJ")
@MenuIcon(VaadinIcons.DOLLAR)
public class ViewProtocoloExameCalculoPj extends ViewPrenotacaoImpl {
    public ViewProtocoloExameCalculoPj() {
        super();
    }
}
