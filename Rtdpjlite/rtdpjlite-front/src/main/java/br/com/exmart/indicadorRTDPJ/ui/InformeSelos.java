package br.com.exmart.indicadorRTDPJ.ui;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.RadioButtonGroup;

/**
 * !! DO NOT EDIT THIS FILE !!
 * <p>
 * This class is generated by Vaadin Designer and will be overwritten.
 * <p>
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class InformeSelos extends VerticalLayout {
    protected RadioButtonGroup<String> groupTipoListarSelo;
    protected Grid<br.com.exmart.rtdpjlite.vo.seloeletronico.InformeSelosVO> seloEnviarGrid;
    protected Button btnInformar;

    public InformeSelos() {
        Design.read(this);
    }
}
