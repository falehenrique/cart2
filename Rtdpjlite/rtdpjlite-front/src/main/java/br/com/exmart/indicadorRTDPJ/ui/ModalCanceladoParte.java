package br.com.exmart.indicadorRTDPJ.ui;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.TextField;
import com.vaadin.ui.HorizontalLayout;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class ModalCanceladoParte extends VerticalLayout {
    protected TextField nome;
    protected TextField documento;
    protected HorizontalLayout panel;
    protected Button btnCancelar;
    protected Button btnImprimir;
    protected Button btnConfirmar;

    public ModalCanceladoParte() {
		Design.read(this);
	}
}
