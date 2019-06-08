package br.com.exmart.indicadorRTDPJ.ui.impl;

import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;

@ViewScope
@SpringView(name = "exame-td")
@MenuCaption("Exame e Cálculo TD")
@MenuIcon(VaadinIcons.DOLLAR)
public class ViewProtocoloExameCalculoTd extends ViewPrenotacaoImpl {
    public ViewProtocoloExameCalculoTd() {
        super();
    }
}
