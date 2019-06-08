package br.com.exmart.indicadorRTDPJ.ui.vo;

import java.time.LocalDateTime;

public class PedidoGridVO {

    private Long idPedido;
    private LocalDateTime data;

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
