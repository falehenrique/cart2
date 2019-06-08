package br.com.exmart.indicadorRTDPJ.ui.component;

import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.util.BeanLocator;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class GridIndicadorReal extends Grid<Protocolo>{

    protected static Logger logger= LoggerFactory.getLogger(GridProtocolo.class);
    private NavigationManager navigationManager;

    public GridIndicadorReal() {
        this.navigationManager = BeanLocator.find(NavigationManager.class);
        setSelectionMode(SelectionMode.SINGLE);
//        addItemClickListener(evt -> {
//            Protocolo protocolo = evt.getItem();
//            if(protocolo.getTipo().equals(TipoProtocolo.PRENOTACAO_PJ)) {
//                navigationManager.navigateTo(ViewPrenotacaoPj.class, evt.getItem().getNumero());
//            }else if(protocolo.getTipo().equals(TipoProtocolo.PRENOTACAO_TD)) {
//                navigationManager.navigateTo(ViewPrenotacaoTd.class, evt.getItem().getNumero());
//            }else if(protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_PJ)) {
//                navigationManager.navigateTo(ViewProtocoloExameCalculoPj.class, evt.getItem().getNumero());
//            }else if(protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_TD)) {
//                navigationManager.navigateTo(ViewProtocoloExameCalculoTd.class, evt.getItem().getNumero());
//            }else{
//                navigationManager.navigateTo(ViewProtocoloCertidaoPj.class, evt.getItem().getNumero());
//            }
//
//
//        });

        addStyleName("gridProtocolo");
        setSizeFull();
//		removeHeaderRow(0);

        // Add stylenames to rows
        setStyleGenerator(GridIndicadorReal::getRowStyle);

        // TODO Arrumar os bindings dos campos após liberação do backend

        // Reg column
        Column<Protocolo, String> registroColumn = addColumn(
                order -> twoRowCellA("Registro nº:",order.getNumeroRegistro(),"Data Reg.:",order.getDataProtocolo()),
                new HtmlRenderer()).setCaption("DATA REG.");

        // Objeto column
        Column<Protocolo, String> objetoColumn = addColumn(
                order -> twoRowCellB("Tipo de objeto:",order.getObjetos().toString(),"Descrição",order.getObjetos().toString()),
                new HtmlRenderer()).setCaption("OBJETO");

        // Parte column
        Column<Protocolo, String> nomeColumn = addColumn(
                order -> twoRowCellC("Parte:",order.getParte(),"CPF/CNPJ:",order.getParteDocumento()),
                new HtmlRenderer()).setCaption("PARTE");

    }

    private void abrirProtocolo() {

    }


    private static String getRowStyle(Protocolo order) {
        return "null";
    }


    // Reg column
    private static String twoRowCellA(String caption1, String numReg, String caption2, LocalDateTime dataReg) {
        return "<div class=\"header\">" + caption1 +
                "</div><div class=\"content\">" + numReg +
                "</div>" + "<div class=\"header\">" + caption2 +
                "</div><div class=\"content2\">" + dataReg +
                "</div>";
    }

    // Objeto column
    private static String twoRowCellB(String caption1, String tipoObjeto, String caption2, String descricao) {
        return "<div class=\"header\">" + caption1 +
                "</div><div class=\"content\">" + tipoObjeto +
                "</div>" + "<div class=\"header\">" + caption2 +
                "</div><div class=\"content2\">" + descricao +
                "</div>";
    }
    // Parte column
    private static String twoRowCellC(String caption1, String parte, String caption2, String docParte) {
        return "</div>" + "<div class=\"header\">" + caption1 +
                "</div><div class=\"content\">" + parte +
                "</div>" + "<div class=\"header\">" + caption2 +
                "</div><div class=\"content2\">" + docParte +
                "</div>";
    }
}
