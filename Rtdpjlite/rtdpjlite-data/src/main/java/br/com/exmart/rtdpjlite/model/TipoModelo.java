package br.com.exmart.rtdpjlite.model;

public enum TipoModelo {
    CARIMBO(1),
    TEXTO(2),
    NOTA_DEVOLUCAO(3),
    APTO_REGISTRO(4),
    TEXTO_CERTIDAO_NOTIFICACAO(5)
    ;

    Integer id;

    TipoModelo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static TipoModelo fromCode(Integer code) {
        for (TipoModelo status :TipoModelo.values()){
            if (status.getId().equals(code)){
                return status;
            }
        }
        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!");
    }
}
