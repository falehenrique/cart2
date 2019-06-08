package br.com.exmart.indicadorRTDPJ.ui;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.ComboBox;
import br.com.exmart.indicadorRTDPJ.ui.component.GridIndicadoresPessoal;
import com.vaadin.ui.CheckBox;

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
public class ModalBusca extends VerticalLayout {
    protected HorizontalLayout panelBusca;
    protected ComboBox<br.com.exmart.indicadorRTDPJ.ui.impl.ModalBuscaImpl.TipoBusca> seletorBusca;
    protected HorizontalLayout buscaReal;
    protected ComboBox<br.com.exmart.rtdpjlite.model.Objeto> comboObjeto;
    protected ComboBox<br.com.exmart.rtdpjlite.model.ObjetoAtributo> comboAtributo;
    protected TextField txtBuscaReal;
    protected Button btnBuscarIndicadorReal;
    protected HorizontalLayout buscaPessoal;
    protected TextField txtBusca;
    protected Button btnBuscar;
    protected Button btnFiltros;
    protected Button btnGerarLinkCompartilhamento;
    protected VerticalLayout panelIndicadorPessoal;
    protected CheckBox ckTipoBuscaNome;
    protected VerticalLayout panelProtocolos;
    protected GridIndicadoresPessoal grid;

    public ModalBusca() {
        Design.read(this);
    }
}
