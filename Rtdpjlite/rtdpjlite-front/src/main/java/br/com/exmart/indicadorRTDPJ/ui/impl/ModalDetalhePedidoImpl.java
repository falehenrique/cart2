package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.ModalDetalhePedido;
import br.com.exmart.indicadorRTDPJ.ui.ModalNovaMensagem;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;
import br.com.exmart.rtdpjlite.vo.balcaoonline.Pedido;
import br.com.exmart.rtdpjlite.vo.balcaoonline.PedidoMensagem;
import br.com.exmart.util.BeanLocator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;

public class ModalDetalhePedidoImpl extends ModalDetalhePedido {
    Window window;
    PedidoService pedidoService;
    private  Pedido idPedido;
    public ModalDetalhePedidoImpl(Pedido idPedido) {
        this.idPedido = idPedido;
        this.pedidoService = BeanLocator.find(PedidoService.class);
        Pedido pedido = null; //FIXME corrigir this.pedidoService.recuperaPedido(idPedido.getId());
        if(pedido != null) {
            if(pedido.getMensagem().size() < 1){
                Notification.show("Atenção","Nenhuma mensagem encontrada para o pedido nº: "+ pedido.getId(), Notification.Type.ERROR_MESSAGE);
            }else {
                for (PedidoMensagem mensagem : pedido.getMensagem()) {
                    CardPedidoTimelineImpl card = new CardPedidoTimelineImpl(mensagem, idPedido);
                    card.setHeight("-1px");
                    listaMensagens.addComponent(card);
                }
            }
        }else{
            Notification.show("Atenção","Pedido de nº " + idPedido + ", não encontrado", Notification.Type.ERROR_MESSAGE);
        }

        btnNovaMensagem.addClickListener(evt->btnNovaMensagemListener(evt));
    }

    private void btnNovaMensagemListener(Button.ClickEvent evt) {
        GerenciarJanela.abrirJanela("",50, 50, new ModalNovaMensagemImpl(this.idPedido.getId()));
    }
}
