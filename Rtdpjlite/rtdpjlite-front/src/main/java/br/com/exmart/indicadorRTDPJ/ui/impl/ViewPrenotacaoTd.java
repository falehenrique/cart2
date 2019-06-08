package br.com.exmart.indicadorRTDPJ.ui.impl;

import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;


@ViewScope
@SpringView(name = "protocolo-td")
@MenuCaption("Registro TD")
@MenuIcon(VaadinIcons.FILE_ADD)
public class ViewPrenotacaoTd extends  ViewPrenotacaoImpl {
    public ViewPrenotacaoTd() {
        super();
    }
}