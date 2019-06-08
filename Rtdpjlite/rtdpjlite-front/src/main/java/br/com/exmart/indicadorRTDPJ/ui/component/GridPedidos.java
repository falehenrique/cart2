package br.com.exmart.indicadorRTDPJ.ui.component;

import br.com.exmart.rtdpjlite.vo.balcaoonline.Pedido;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import com.vaadin.ui.Grid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridPedidos extends Grid<Pedido>{

    protected static Logger logger= LoggerFactory.getLogger(GridPedidos.class);
    private NavigationManager navigationManager;

    public GridPedidos() {
//        this.navigationManager = BeanLocator.find(NavigationManager.class);
//        setSelectionMode(SelectionMode.SINGLE);
////        addItemClickListener(evt -> {
////            Protocolo protocolo = evt.getItem();
////            if(protocolo.getTipo().equals(TipoProtocolo.PRENOTACAO_PJ)) {
////                navigationManager.navigateTo(ViewPrenotacaoPj.class, evt.getItem().getNumero());
////            }else if(protocolo.getTipo().equals(TipoProtocolo.PRENOTACAO_TD)) {
////                navigationManager.navigateTo(ViewPrenotacaoTd.class, evt.getItem().getNumero());
////            }else if(protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_PJ)) {
////                navigationManager.navigateTo(ViewProtocoloExameCalculoPj.class, evt.getItem().getNumero());
////            }else if(protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_TD)) {
////                navigationManager.navigateTo(ViewProtocoloExameCalculoTd.class, evt.getItem().getNumero());
////            }else{
////                navigationManager.navigateTo(ViewProtocoloCertidaoPj.class, evt.getItem().getNumero());
////            }
////
////
////        });
//
//        addStyleName("gridProtocolo");
//        setSizeFull();
////		removeHeaderRow(0);
//
//        // Add stylenames to rows
//        setStyleGenerator(GridPedidos::getRowStyle);
//
//        // TODO Arrumar os bindings dos campos após liberação do backend
//
//        // Icon column
//        Column<PedidoGridVO, String> iconColumn = addColumn(
//                order -> twoRowCellIcon(VaadinIcons.CART.getHtml(),"Nº do Pedido",order.getIdPedido()),
//                new HtmlRenderer()).setCaption("PASTA PJ").setWidth(110);
//
//        // Nome column
//        Column<Protocolo, String> nomeColumn = addColumn(
//                order -> twoRowCellB("Nome",order.getParte()),
//                new HtmlRenderer()).setCaption("NOME");
//
//        // Doc column
//        Column<Protocolo, String> documentoColumn = addColumn(
//                order -> twoRowCellC("CPF/CNPJ:",order.getParteDocumento()),
//                new HtmlRenderer()).setCaption("CPF/CNPJ");
//
//        // Registro column
//        Column<Protocolo, String> registroColumn = addColumn(
//                order -> twoRowCellD("Nº Pedido:",order.getNumero(),"Data Pedido:",order.getDataProtocolo()),
//                new HtmlRenderer()).setCaption("DATA REG.");
//
//        // Qualificação column
//        Column<Protocolo, String> qualificacaoColumn = addColumn(
//                order -> twoRowCellE("Apresentante:",order.getApresentante().toString(),"Pasta PJ",order.getPastaPJ()),
//                new HtmlRenderer()).setCaption("PASTA PJ");
//
//    }
//
//    private void abrirProtocolo() {
//
//    }
//
//
//    private static String getRowStyle(Protocolo order) {
//        return "null";
//    }
//
//
//    // Icon column
//    private static String twoRowCellIcon(String icon, String caption1, Long pastaPJ) {
//        return "<div class=\"headerIcon\">" + icon +
//                "<div class=\"header\">" + caption1 +
//                "</div><div class=\"contentIcon\">" + pastaPJ +
//                "</div>";
//    }
//
//    private static String twoRowCellB(String caption, String parte) {
//        return "</div>" + "<div class=\"header\">" + caption +
//                "</div><div class=\"content2\">" + parte +
//                "</div>";
//    }
//    private static String twoRowCellC(String caption, String cpfParte) {
//        return "<div class=\"header\">" + caption +
//                "</div><div class=\"content2\">" + cpfParte +
//                "</div>";
//    }
//    private static String twoRowCellD(String caption1, Long numReg, String caption2, Date dataReg) {
//        return "<div class=\"header\">" + caption1 +
//                "</div><div class=\"content\">" + numReg +
//                "</div>" + "<div class=\"header\">" + caption2 +
//                "</div><div class=\"content2\">" + dataReg +
//                "</div>";
//    }
//    private static String twoRowCellE(String caption1, String qualificacao, String caption2, String pastaPJ) {
//        return "<div class=\"header\">" + caption1 +
//                "</div><div class=\"content\">" + qualificacao +
//                "</div>" + "<div class=\"header\">" + caption2 +
//                "</div><div class=\"content2\">" + pastaPJ +
//                "</div>";
    }
}
