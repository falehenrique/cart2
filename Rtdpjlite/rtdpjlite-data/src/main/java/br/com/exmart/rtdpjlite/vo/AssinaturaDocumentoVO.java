package br.com.exmart.rtdpjlite.vo;

public class AssinaturaDocumentoVO {
    private String emissor;
    private String dtEmissao;
    private String signatario;
    private String motivo;

    public AssinaturaDocumentoVO(String emissor, String dtEmissao, String signatario, String motivo) {
        this.emissor = emissor;
        this.dtEmissao = dtEmissao;
        this.signatario = signatario;
        this.motivo = motivo;
    }

    public String getEmissor() {
        return emissor;
    }

    public void setEmissor(String emissor) {
        this.emissor = emissor;
    }

    public String getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(String dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getSignatario() {
        return signatario;
    }

    public void setSignatario(String signatario) {
        this.signatario = signatario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
