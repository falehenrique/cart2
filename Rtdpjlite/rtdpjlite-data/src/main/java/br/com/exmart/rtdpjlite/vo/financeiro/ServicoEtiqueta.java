package br.com.exmart.rtdpjlite.vo.financeiro;

public class ServicoEtiqueta {
    private Long idServicoEtiqueta;
    private String nome;
    private String texto;
    private boolean icPadrao;

    public ServicoEtiqueta(Long idServicoEtiqueta, String nome, String texto, boolean icPadrao) {
        this.idServicoEtiqueta = idServicoEtiqueta;
        this.nome = nome;
        this.texto = texto;
        this.icPadrao = icPadrao;
    }

    public ServicoEtiqueta() {
    }

    public Long getIdServicoEtiqueta() {
        return idServicoEtiqueta;
    }

    public void setIdServicoEtiqueta(Long idServicoEtiqueta) {
        this.idServicoEtiqueta = idServicoEtiqueta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }


    public boolean isIcPadrao() {
        return icPadrao;
    }

    public void setIcPadrao(boolean icPadrao) {
        this.icPadrao = icPadrao;
    }
}
