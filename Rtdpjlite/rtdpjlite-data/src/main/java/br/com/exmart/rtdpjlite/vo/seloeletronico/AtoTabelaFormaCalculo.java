package br.com.exmart.rtdpjlite.vo.seloeletronico;

public class AtoTabelaFormaCalculo {
    private String nomeDivisor;
    private String sgTabelaCustas;

    public AtoTabelaFormaCalculo(String nomeDivisor, String sgTabelaCustas) {
        this.nomeDivisor = nomeDivisor;
        this.sgTabelaCustas = sgTabelaCustas;
    }

    public AtoTabelaFormaCalculo() {
    }

    public String getNomeDivisor() {
        return nomeDivisor;
    }

    public void setNomeDivisor(String nomeDivisor) {
        this.nomeDivisor = nomeDivisor;
    }

    public String getSgTabelaCustas() {
        return sgTabelaCustas;
    }

    public void setSgTabelaCustas(String sgTabelaCustas) {
        this.sgTabelaCustas = sgTabelaCustas;
    }
}
