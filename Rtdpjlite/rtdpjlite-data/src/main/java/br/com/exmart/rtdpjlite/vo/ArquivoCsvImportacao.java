package br.com.exmart.rtdpjlite.vo;

import com.google.common.base.Strings;
import com.opencsv.bean.CsvBindByPosition;

public class ArquivoCsvImportacao {
    @CsvBindByPosition(position = 0)
    private String natureza;
    @CsvBindByPosition(position = 1)
    private String parte;
    @CsvBindByPosition(position = 2)
    private String parteIgnorar;
    @CsvBindByPosition(position = 3)
    private String parte2;
    @CsvBindByPosition(position = 4)
    private String parte2Documento;
    @CsvBindByPosition(position = 5)
    private String dataDocumento;
    @CsvBindByPosition(position = 6)
    private String arquivo;
    @CsvBindByPosition(position = 7)
    private String registro;
    @CsvBindByPosition(position = 8)
    private String dataRegistro;

    private Long protocoloId;

    public ArquivoCsvImportacao(String natureza, String parte, String parteIgnorar, String parte2, String parte2Documento, String dataDocumento, String arquivo, String registro, String dataRegistro) {
        this.natureza = natureza;
        this.parte = parte;
        this.parteIgnorar = parteIgnorar;
        this.parte2 = parte2;
        this.parte2Documento = parte2Documento;
        this.dataDocumento = dataDocumento;
        this.arquivo = arquivo;
        this.registro = registro;
        this.dataRegistro = dataRegistro;
    }

    public ArquivoCsvImportacao() {
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public String getParte() {
        return parte;
    }

    public void setParte(String parte) {
        this.parte = parte;
    }

    public String getParte2() {
        return parte2;
    }

    public void setParte2(String parte2) {
        this.parte2 = parte2;
    }

    public String getParte2Documento() {
        return parte2Documento;
    }

    public void setParte2Documento(String parte2Documento) {
        this.parte2Documento = parte2Documento;
    }

    public String getDataDocumento() {
        return dataDocumento;
    }

    public void setDataDocumento(String dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public String toString() {
        return "ArquivoCsvImportacao{" +
                "natureza='" + natureza + '\'' +
                ", parte='" + parte + '\'' +
                ", parte2='" + parte2 + '\'' +
                ", parte2Documento='" + parte2Documento + '\'' +
                ", dataDocumento='" + dataDocumento + '\'' +
                ", arquivo='" + arquivo + '\'' +
                '}';
    }

    public Long getProtocoloId() {
        return protocoloId;
    }

    public void setProtocoloId(Long protocoloId) {
        this.protocoloId = protocoloId;
    }

    public String getParteIgnorar() {
        return parteIgnorar;
    }

    public void setParteIgnorar(String parteIgnorar) {
        this.parteIgnorar = parteIgnorar;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public boolean contemCampoNull(){
        if(Strings.isNullOrEmpty(this.natureza))
                return true;
        if(Strings.isNullOrEmpty(this.parte))
            return true;
        if(Strings.isNullOrEmpty(this.parteIgnorar))
            return true;
        if(Strings.isNullOrEmpty(this.parte2))
            return true;
        if(Strings.isNullOrEmpty(this.parte2Documento))
            return true;
        if(Strings.isNullOrEmpty(this.dataDocumento))
            return true;
        if(Strings.isNullOrEmpty(this.arquivo))
            return true;

        return false;
    }
}
