package br.com.exmart.indicadorRTDPJ.ui.view;

import br.com.exmart.indicadorRTDPJ.ui.impl.ModalBuscaImpl;
import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;

@SpringView(name = "indicadores")
@UIScope
@MenuCaption("INDICADORES")
@MenuIcon(VaadinIcons.USER_CARD)
public class BuscarIndicadorView  extends ModalBuscaImpl implements View{

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        gerarTipoBusca(true);
    }
}
