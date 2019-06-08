package br.com.exmart.rtdpjlite.vo.indisponibilidade;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class IndisponibilidadeVO {
    private Long id;

    private String protocoloIndisponibilidade;

    private String protocoloCancelamentoIndisponibilidade;

    private Long cancelamentoTipo;

    private LocalDateTime cancelamentoData;

    private LocalDateTime dataPedido;

    private String numeroProcesso;

    private String telefone;

    private String nomeInstituicao;

    private String forumVara;

    private String usuario;

    private String email;

    private LocalDateTime dataImportacao;

    private Set<PessoaXMLVO> partes;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProtocoloIndisponibilidade() {
        return protocoloIndisponibilidade;
    }

    public void setProtocoloIndisponibilidade(String protocoloIndisponibilidade) {
        this.protocoloIndisponibilidade = protocoloIndisponibilidade;
    }

    public String getProtocoloCancelamentoIndisponibilidade() {
        return protocoloCancelamentoIndisponibilidade;
    }

    public void setProtocoloCancelamentoIndisponibilidade(String protocoloCancelamentoIndisponibilidade) {
        this.protocoloCancelamentoIndisponibilidade = protocoloCancelamentoIndisponibilidade;
    }

    public Long getCancelamentoTipo() {
        return cancelamentoTipo;
    }

    public void setCancelamentoTipo(Long cancelamentoTipo) {
        this.cancelamentoTipo = cancelamentoTipo;
    }

    public LocalDateTime getCancelamentoData() {
        return cancelamentoData;
    }

    public void setCancelamentoData(LocalDateTime cancelamentoData) {
        this.cancelamentoData = cancelamentoData;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNomeInstituicao() {
        return nomeInstituicao;
    }

    public void setNomeInstituicao(String nomeInstituicao) {
        this.nomeInstituicao = nomeInstituicao;
    }

    public String getForumVara() {
        return forumVara;
    }

    public void setForumVara(String forumVara) {
        this.forumVara = forumVara;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDataImportacao() {
        return dataImportacao;
    }

    public void setDataImportacao(LocalDateTime dataImportacao) {
        this.dataImportacao = dataImportacao;
    }

    public Set<PessoaXMLVO> getPartes() {
        return partes;
    }

    public void setPartes(Set<PessoaXMLVO> partes) {
        this.partes = partes;
    }
}
