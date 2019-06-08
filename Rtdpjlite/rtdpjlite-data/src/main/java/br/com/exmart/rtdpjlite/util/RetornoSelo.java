package br.com.exmart.rtdpjlite.util;

/**
 * Created by Heryk on 28/09/2018.
 */
public class RetornoSelo {
    private String urlTribunalJustica;
    private String seloDigital;
    private String total;
    private String iss;
    private String assinaturaDigital;
    private String qrCode;
    private String mensagemCarimboSelo;
    private Integer seloDigitalId;

    public RetornoSelo() {
    }

    public RetornoSelo(String urlTribunalJustica, String seloDigital, String total, String iss, String assinaturaDigital, String qrCode, String mensagemCarimboSelo, Integer seloDigitalId) {
        this.urlTribunalJustica = urlTribunalJustica;
        this.seloDigital = seloDigital;
        this.total = total;
        this.iss = iss;
        this.assinaturaDigital = assinaturaDigital;
        this.qrCode = qrCode;
        this.mensagemCarimboSelo = mensagemCarimboSelo;
        this.seloDigitalId = seloDigitalId;
    }

    public String getUrlTribunalJustica() {
        return urlTribunalJustica;
    }

    public void setUrlTribunalJustica(String urlTribunalJustica) {
        this.urlTribunalJustica = urlTribunalJustica;
    }

    public String getSeloDigital() {
        return seloDigital;
    }

    public void setSeloDigital(String seloDigital) {
        this.seloDigital = seloDigital;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getAssinaturaDigital() {
        return assinaturaDigital;
    }

    public void setAssinaturaDigital(String assinaturaDigital) {
        this.assinaturaDigital = assinaturaDigital;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getMensagemCarimboSelo() {
        return mensagemCarimboSelo;
    }

    public void setMensagemCarimboSelo(String mensagemCarimboSelo) {
        this.mensagemCarimboSelo = mensagemCarimboSelo;
    }

    public Integer getSeloDigitalId() {
        return seloDigitalId;
    }

    public void setSeloDigitalId(Integer seloDigitalId) {
        this.seloDigitalId = seloDigitalId;
    }
}
