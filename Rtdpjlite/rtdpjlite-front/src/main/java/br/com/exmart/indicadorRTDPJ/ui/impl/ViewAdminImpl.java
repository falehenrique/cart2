package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.ViewAdmin;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;

@SpringView(name = "admin")
@UIScope

public class ViewAdminImpl extends ViewAdmin implements View{

    public ViewAdminImpl() {

        btnPesquisarCustas.addClickListener(evt-> abrirCustas());

    }

    private void abrirCustas() {
        GerenciarJanela.abrirJanela("",90,90,new ModalTabelaCustasImpl());
    }
}
