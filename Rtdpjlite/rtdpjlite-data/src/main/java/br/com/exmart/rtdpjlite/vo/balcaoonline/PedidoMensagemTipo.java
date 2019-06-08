package br.com.exmart.rtdpjlite.vo.balcaoonline;

import java.util.ArrayList;
import java.util.List;

public class PedidoMensagemTipo {
    private Long id;
    private String nome;

    public PedidoMensagemTipo(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public PedidoMensagemTipo() {
    }

    public PedidoMensagemTipo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static List<PedidoMensagemTipo> listar(){
        List<PedidoMensagemTipo> retorno = new ArrayList<>();
        retorno.add(new PedidoMensagemTipo(4L,"AGUARDANDO PAGAMENTO"));
        retorno.add(new PedidoMensagemTipo(7L,"AGUARDANDO PROTOCOLAR"));
        retorno.add(new PedidoMensagemTipo(6L,"AVERBADA"));
        retorno.add(new PedidoMensagemTipo(9L,"CANCEL. PELA PARTE"));
        retorno.add(new PedidoMensagemTipo(3L,"CERTIDÃO ENCAMINHADA"));
        retorno.add(new PedidoMensagemTipo(5L,"PAGO PELO CLIENTE"));
        retorno.add(new PedidoMensagemTipo(1L,"PROTOCOLADO"));
        retorno.add(new PedidoMensagemTipo(2L,"REGISTRADO"));
        retorno.add(new PedidoMensagemTipo(8L,"SOLICITAR CANCELAMENTO"));
        retorno.add(new PedidoMensagemTipo(10L,"CONCLUIDO"));
        return retorno;
    }
}
