package br.com.exmart.indicadorRTDPJ.ui.impl;

import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;

@ViewScope
@SpringView(name = "certidao-pj")
@MenuCaption("Certidão PJ")
@MenuIcon(VaadinIcons.FILE_PICTURE)
public class ViewProtocoloCertidaoPj extends ViewPrenotacaoImpl{
    public ViewProtocoloCertidaoPj() {
        super();
    }
}
