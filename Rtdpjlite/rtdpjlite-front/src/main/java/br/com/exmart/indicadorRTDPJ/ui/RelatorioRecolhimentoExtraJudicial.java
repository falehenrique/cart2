package br.com.exmart.indicadorRTDPJ.ui;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button;

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
public class RelatorioRecolhimentoExtraJudicial extends VerticalLayout {
    protected DateField dtInicial;
    protected DateField dtFinal;
    protected HorizontalLayout tete;
    protected Button btnFechar;
    protected Button btnGerarRelatorio;

    public RelatorioRecolhimentoExtraJudicial() {
        Design.read(this);
    }
}
