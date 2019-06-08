package br.com.exmart.indicadorRTDPJ.service.rest.vo;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by fabioebner on 28/05/17.
 */
public class ServicoVO {
    private Long id;
    @NotEmpty
    private String nome;
    @NotEmpty
    private String tabela;
    @NotNull
    private boolean icCertidao;

    public ServicoVO() {
    }

    public ServicoVO(Long id, String nome, String tabela, boolean icCertidao) {
        this.id = id;
        this.nome = nome;
        this.tabela = tabela;
        this.icCertidao = icCertidao;
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

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public boolean isIcCertidao() {
        return icCertidao;
    }

    public void setIcCertidao(boolean icCertidao) {
        this.icCertidao = icCertidao;
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
