package br.com.exmart.indicadorRTDPJ.ui.component;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;

import br.com.exmart.indicadorRTDPJ.ui.impl.ViewRegistroImpl;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.rtdpjlite.model.IndicadorPessoalVO;
import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.RegistroService;
import br.com.exmart.util.BeanLocator;

public class GridIndicadoresPessoal extends Grid<IndicadorPessoalVO>{

    private static final long serialVersionUID = 1L;

	public GridIndicadoresPessoal() {
    	 addColumn(order -> order.getParte().getNome()).setCaption("NOME").setMinimumWidthFromContent(true).setExpandRatio(0);
         addColumn(order -> order.getParte().getDocumento()).setCaption("CPF/CNPJ").setMinimumWidthFromContent(true).setExpandRatio(0);
         addColumn(order -> order.getNumeroContrato()).setCaption("NUMERO CONTRATO").setExpandRatio(0);
         addColumn(order -> Utils.formatarDataComHora(order.getDataContrato())).setCaption("DATA DOC.").setExpandRatio(0);
         addColumn(order -> Long.parseLong(order.getNumeroRegistro())).setCaption("NUMERO REGISTRO").setExpandRatio(0);
         addColumn(order -> Utils.formatarDataComHora(order.getDtRegistro())).setCaption("DATA REGISTRO").setExpandRatio(0);
         addColumn(order -> order.getObservacao()).setCaption("OBSERVAÇÃO").setExpandRatio(0);
         addColumn(order -> order.getSubnatureza()).setCaption("SUBNATUREZA").setExpandRatio(0);
         addColumn(order -> order.getValor()).setCaption("VALOR DOCUMENTO").setExpandRatio(0);

         
         addComponentColumn(registro -> {
             Button button = new Button(VaadinIcons.ARCHIVE);
             button.addStyleName("borderless");
             button.addClickListener(click -> {
            	 Registro  registrodb = BeanLocator.find(RegistroService.class).findByRegistroAndEspecialidade(registro.getRegistro(), registro.getEspecialidade());
                 GerenciarJanela.abrirJanela("", 90, 90, new ViewRegistroImpl(registrodb));
             });
             return button;
         }).setWidth(100);

         addComponentColumn(registro -> {
             Button button = new Button(VaadinIcons.LIST_UL);
             button.addStyleName("borderless");
             button.addClickListener(click -> {
             	NavigationManager navigator = new NavigationManager();
             	navigator.navegarPara(TipoProtocolo.recuperaTipoPrenotacaoByEspecialidade(registro.getEspecialidade()),registro.getProtocolo());
             });
             return button;
         }).setWidth(100);

    }
}