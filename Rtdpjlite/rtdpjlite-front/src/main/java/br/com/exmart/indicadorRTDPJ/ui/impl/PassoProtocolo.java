package br.com.exmart.indicadorRTDPJ.ui.impl;

public interface PassoProtocolo {
    public void habilitarForm(boolean habilitar, boolean icProtocoloEditavel);
    public void cancelar();
    public void focus();
    public void validate() throws Exception;
    public void removerListener();
    public void addListener();

}
