package br.com.exmart.indicadorRTDPJ.service.rest.vo;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by fabioebner on 27/05/17.
 */
public class ProtocoloNaturezaVO {
    private Long id;
    @NotEmpty
    private String nome;
    @NotNull
    private int diasPrevisao;
    @NotNull
    private int diasVencimento;

    public ProtocoloNaturezaVO() {
    }

    public ProtocoloNaturezaVO(Long id, String nome, int diasPrevisao, int diasVencimento) {
        this.id = id;
        this.nome = nome;
        this.diasPrevisao = diasPrevisao;
        this.diasVencimento = diasVencimento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDiasPrevisao() {
        return diasPrevisao;
    }

    public void setDiasPrevisao(int diasPrevisao) {
        this.diasPrevisao = diasPrevisao;
    }

    public int getDiasVencimento() {
        return diasVencimento;
    }

    public void setDiasVencimento(int diasVencimento) {
        this.diasVencimento = diasVencimento;
    }
}
