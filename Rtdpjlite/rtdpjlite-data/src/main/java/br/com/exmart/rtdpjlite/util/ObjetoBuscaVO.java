package br.com.exmart.rtdpjlite.util;

public class ObjetoBuscaVO {

    private Long codObjeto;
    private String nomeCampo;
    private String valorCampo;

    public ObjetoBuscaVO(Long codObjeto, String nomeCampo, String valorCampo) {
        this.codObjeto = codObjeto;
        this.nomeCampo = nomeCampo != null? nomeCampo.toUpperCase():nomeCampo;
        this.valorCampo = valorCampo != null? valorCampo.toUpperCase():valorCampo;
    }

    public ObjetoBuscaVO() {
    }

    public Long getCodObjeto() {
        return codObjeto;
    }

    public void setCodObjeto(Long codObjeto) {
        this.codObjeto = codObjeto;
    }

    public String getNomeCampo() {
        return nomeCampo;
    }

    public void setNomeCampo(String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }

    public String getValorCampo() {
        return valorCampo;
    }

    public void setValorCampo(String valorCampo) {
        this.valorCampo = valorCampo;
    }
}
