package br.com.exmart.indicadorRTDPJ.ui;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalSplitPanel;

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
public class ViewPrenotacao extends VerticalLayout {
    protected Label labelTituloProtocolo;
    protected Label labelNumeroProtocolo;
    protected Label labelDataProtocolo;
    protected ComboBox<br.com.exmart.rtdpjlite.model.Status> status;
    protected Button btnStatus;
    protected Button btnDuplicarProtocolo;
    protected HorizontalLayout panelAlertas;
    protected VerticalSplitPanel splitProtocolo;
    protected TabSheet conteudo;
    protected HorizontalLayout panelPdf;
    protected HorizontalLayout panelFerramentas;
    protected Button btnVerTitulo;
    protected MenuBar menuImpressao;
    protected HorizontalLayout panelEditarExame;
    protected Button btnCancelar;
    protected Button btnEditar;
    protected Button btnSalvar;

    public ViewPrenotacao() {
		Design.read(this);
	}
}
