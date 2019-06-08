package br.com.exmart.rtdpjlite.model.listener;

import br.com.exmart.rtdpjlite.model.portal.Pedido;
import br.com.exmart.rtdpjlite.model.portal.PedidoProtocolo;
import br.com.exmart.rtdpjlite.model.portal.PedidoProtocoloStatus;
import br.com.exmart.rtdpjlite.model.portal.PedidoStatus;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class PedidoListener {
    @PrePersist
    @PreUpdate
    public void atualizarStatusAtualCartorio(final Pedido pedido){
        Pedido.StatusAtualCartorio statusAtualCartorio = Pedido.StatusAtualCartorio.AGUARDE;
        int totalAverbacaoRegistrada = 0;
        for(PedidoStatus status : pedido.getStatusPedido()){
            if(status.getData() == null){
                statusAtualCartorio = Pedido.StatusAtualCartorio.INTERAGIR;
            }
            if(status.getNomeAguardando().equalsIgnoreCase(PedidoService.StatusPedido.AVERBACAO.getNomeAguardando())){
                if(status.getData() != null)
                    totalAverbacaoRegistrada++;
            }
        }

        int totalProtocolosFinalizados = 0;
        for(PedidoProtocolo pedidoProtocolo : pedido.getProtocolos()){
            for(PedidoProtocoloStatus pedidoProtocoloStatus: pedidoProtocolo.getStatus()){
                if(pedidoProtocoloStatus.getNomeAguardando().equalsIgnoreCase(PedidoService.StatusPedido.REGISTRO.getNomeAguardando())){
                    if(pedidoProtocoloStatus.getData() != null)
                        totalProtocolosFinalizados++;
                }
            }
        }
        if(totalAverbacaoRegistrada != totalProtocolosFinalizados)
            statusAtualCartorio = Pedido.StatusAtualCartorio.INTERAGIR;

        pedido.setStatusAtualCartorio(statusAtualCartorio.getNome());
        if(pedido.getDtFinalizado() != null){
            pedido.setStatusAtualCartorio(Pedido.StatusAtualCliente.PEDIDO_CONCLUIDO.getNome());
            pedido.setStatusAtualCliente(Pedido.StatusAtualCliente.PEDIDO_CONCLUIDO.getNome());
        }
    }
}
