package br.com.exmart.rtdpjlite.vo.indisponibilidade;

import java.time.LocalDateTime;
import java.util.List;

public class ParteIndisponivel {
    private Long idParte;
    private Long idOficio;
    private String tipo;
    private String nome;
    private String nomeFonetico;
    private String documento;
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

    private List<ParteIndisponivelRegistro> registros;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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

    public List<ParteIndisponivelRegistro> getRegistros() {
        return registros;
    }

    public void setRegistros(List<ParteIndisponivelRegistro> registros) {
        this.registros = registros;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ParteIndisponivel(Long idParte, String nome,String nomeFonetico, String documento, String protocoloIndisponibilidade, String protocoloCancelamentoIndisponibilidade, Long cancelamentoTipo, LocalDateTime cancelamentoData, LocalDateTime dataPedido, String numeroProcesso, String telefone, String nomeInstituicao, String forumVara, String usuario, String email, List<ParteIndisponivelRegistro> registros, String tipo, Long idOficio) {
        this.idParte = idParte;
        this.nome = nome;
        this.nomeFonetico = nomeFonetico;
        this.documento = documento;
        this.protocoloIndisponibilidade = protocoloIndisponibilidade;
        this.protocoloCancelamentoIndisponibilidade = protocoloCancelamentoIndisponibilidade;
        this.cancelamentoTipo = cancelamentoTipo;
        this.cancelamentoData = cancelamentoData;
        this.dataPedido = dataPedido;
        this.numeroProcesso = numeroProcesso;
        this.telefone = telefone;
        this.nomeInstituicao = nomeInstituicao;
        this.forumVara = forumVara;
        this.usuario = usuario;
        this.email = email;
        this.registros = registros;
        this.tipo = tipo;
        this.idOficio = idOficio;
    }


    public String getNomeFonetico() {
        return nomeFonetico;
    }

    public void setNomeFonetico(String nomeFonetico) {
        this.nomeFonetico = nomeFonetico;
    }

    public Long getIdOficio() {
        return idOficio;
    }

    public void setIdOficio(Long idOficio) {
        this.idOficio = idOficio;
    }

    public Long getIdParte() {
        return idParte;
    }

    public void setIdParte(Long idParte) {
        this.idParte = idParte;
    }

    @Override
    public String toString() {
        return "ParteIndisponivel{" +
                "idParte=" + idParte +
                ", idOficio=" + idOficio +
                ", tipo='" + tipo + '\'' +
                ", nome='" + nome + '\'' +
                ", nomeFonetico='" + nomeFonetico + '\'' +
                ", documento='" + documento + '\'' +
                ", protocoloIndisponibilidade='" + protocoloIndisponibilidade + '\'' +
                ", protocoloCancelamentoIndisponibilidade='" + protocoloCancelamentoIndisponibilidade + '\'' +
                ", cancelamentoTipo=" + cancelamentoTipo +
                ", cancelamentoData=" + cancelamentoData +
                ", dataPedido=" + dataPedido +
                ", numeroProcesso='" + numeroProcesso + '\'' +
                ", telefone='" + telefone + '\'' +
                ", nomeInstituicao='" + nomeInstituicao + '\'' +
                ", forumVara='" + forumVara + '\'' +
                ", usuario='" + usuario + '\'' +
                ", email='" + email + '\'' +
                ", registros=" + registros +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ParteIndisponivel) {
            ParteIndisponivel parte = (ParteIndisponivel) obj;
            return (this.getIdOficio() == parte.getIdOficio() & this.getTipo() == parte.getTipo() & this.getIdParte() == parte.getIdParte());
        }
        return false;
    }
}

