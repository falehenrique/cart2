package br.com.exmart.rtdpjlite.vo.indisponibilidade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

public class PessoaIndisponibilidadeVO {
    private PessoaIndisponivelVO pessoaIndisponivel;
    private IndisponibilidadeXMLVO indisponibilidadeXml;

    public PessoaIndisponivelVO getPessoaIndisponivel() {
        return pessoaIndisponivel;
    }

    public void setPessoaIndisponivel(PessoaIndisponivelVO pessoaIndisponivel) {
        this.pessoaIndisponivel = pessoaIndisponivel;
    }

    public IndisponibilidadeXMLVO getIndisponibilidadeXml() {
        return indisponibilidadeXml;
    }

    public void setIndisponibilidadeXml(IndisponibilidadeXMLVO indisponibilidadeXml) {
        this.indisponibilidadeXml = indisponibilidadeXml;
    }
}
