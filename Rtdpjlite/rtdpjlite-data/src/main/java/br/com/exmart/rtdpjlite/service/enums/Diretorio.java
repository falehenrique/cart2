package br.com.exmart.rtdpjlite.service.enums;

public enum Diretorio {
    NOTA_DEVOLUCAO("NOTA_DEVOLUCAO"),
    REGISTRO("REGISTRO"),
    PROTOCOLO("PROTOCOLO"),
    CARIMBADO("CARIMBADO"),
    ASSINADO("ASSINADO"),
    ANDAMENTO("ANDAMENTOS"),
    PEDIDO("Pedido"),
    PROTOCOLO_EXAME_CALCULO("EXAME_CALCULO"),
    PROTOCOLO_PRENOTACAO("PRENOTACAO"),
    PROTOCOLO_CERTIDAO("CERTIDAO"),
    CERTIDAO_NOTIFICACAO("CERTIDAO_NOTIFICACAO")
    ;


    String diretorio;

    Diretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    public String getDiretorio() {
        return diretorio;
    }
}
