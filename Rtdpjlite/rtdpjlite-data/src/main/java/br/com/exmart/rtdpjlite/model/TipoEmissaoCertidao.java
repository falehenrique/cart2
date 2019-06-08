package br.com.exmart.rtdpjlite.model;

public enum TipoEmissaoCertidao {
    TEXTO(10),
    IMAGEM(20),
    IMAGEM_INTEIRO_TEOR(30),
    TEXTO_NOTIFICACAO(40);
    Integer id;

    TipoEmissaoCertidao(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static String nomeFromCode(Integer code){
        if(code.equals(10)){
            return "TEXTO";
        }else if(code.equals(20)){
            return "IMAGEM";
        }else if(code.equals(30)){
            return "IMAGEM_INTEIRO_TEOR";
        }else{
            return null;
        }
    }

    public static TipoEmissaoCertidao fromCode(Integer code) {
        for (TipoEmissaoCertidao status :TipoEmissaoCertidao.values()){
            if (status.getId().equals(code)){
                return status;
            }
        }
        throw new UnsupportedOperationException(
                "The code " + code + " is not supported!");
    }

    public static boolean isImagem(TipoEmissaoCertidao tipo){
        if(tipo == null){
            return false;
        }
        if(tipo.equals(TipoEmissaoCertidao.IMAGEM)){
            return true;
        }
        if(tipo.equals(TipoEmissaoCertidao.IMAGEM_INTEIRO_TEOR)){
            return true;
        }
        return false;
    }
}
