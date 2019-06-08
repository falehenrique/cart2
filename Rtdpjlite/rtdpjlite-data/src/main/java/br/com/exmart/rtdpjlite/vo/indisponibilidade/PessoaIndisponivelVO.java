package br.com.exmart.rtdpjlite.vo.indisponibilidade;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class PessoaIndisponivelVO {

    private String nome;

    private String nomeFonetico;

    private String cpfCnpj;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeFonetico() {
        return nomeFonetico;
    }

    public void setNomeFonetico(String nomeFonetico) {
        this.nomeFonetico = nomeFonetico;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }
}
