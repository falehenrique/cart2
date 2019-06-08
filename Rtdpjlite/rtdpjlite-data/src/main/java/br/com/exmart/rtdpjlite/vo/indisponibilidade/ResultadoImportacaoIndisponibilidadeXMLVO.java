package br.com.exmart.rtdpjlite.vo.indisponibilidade;

public class ResultadoImportacaoIndisponibilidadeXMLVO {
    private int qtdIndisponibilidade;
    private int qtdCancelamento;
    private int qtdTotal;

    public int getQtdIndisponibilidade() {
        return qtdIndisponibilidade;
    }

    public void setQtdIndisponibilidade(int qtdIndisponibilidade) {
        this.qtdIndisponibilidade = qtdIndisponibilidade;
    }

    public int getQtdCancelamento() {
        return qtdCancelamento;
    }

    public void setQtdCancelamento(int qtdCancelamento) {
        this.qtdCancelamento = qtdCancelamento;
    }

    public int getQtdTotal() {
        return qtdTotal;
    }

    public void setQtdTotal(int qtdTotal) {
        this.qtdTotal = qtdTotal;
    }

    @Override
    public String toString() {
        return  "Indisponibilidade = " + qtdIndisponibilidade +
                ", Cancelamentos = " + qtdCancelamento +
                ", Total =" + qtdTotal;
    }
}
