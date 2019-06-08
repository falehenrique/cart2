package br.com.exmart.rtdpjlite.vo;

import br.com.exmart.rtdpjlite.annotations.CpfCnpj;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class StatusProtocoloJson implements Serializable {

    private boolean possuiAnexo;
    private String observacao;


    //Devolução de Titulo
    @NotEmpty
    private String texto;

    //Reentrada de titulo
    @NotEmpty
    private String apresentanteReentrada;
    private String telefoneReentrada;
    @Email
    private String emailReentrada;
    private boolean icProcedenteReentrada;


    //Entrega de titulo
    @NotEmpty
    private String nomeRetirante;
    @NotEmpty
    private String cpfRetirante;
    @NotNull
    private LocalDateTime dataRetirada;


    //Cancelamento parte
    @NotEmpty
    private String nomeParte;
    @CpfCnpj
    @NotEmpty
    private String documentoParte;
    private String requerimento;


    //Cancelamento Judicial
    @NotEmpty
    private String nrOrdem;
    @NotNull
    private LocalDateTime dataCancelamentoJudicial;


    //Suscitação de Duvida
    @NotEmpty
    private String nrProcessoDuvida;

    public boolean isPossuiAnexo() {
        return possuiAnexo;
    }

    public void setPossuiAnexo(boolean possuiAnexo) {
        this.possuiAnexo = possuiAnexo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getApresentanteReentrada() {
        return apresentanteReentrada;
    }

    public void setApresentanteReentrada(String apresentanteReentrada) {
        this.apresentanteReentrada = apresentanteReentrada;
    }

    public String getTelefoneReentrada() {
        return telefoneReentrada;
    }

    public void setTelefoneReentrada(String telefoneReentrada) {
        this.telefoneReentrada = telefoneReentrada;
    }

    public String getEmailReentrada() {
        return emailReentrada;
    }

    public void setEmailReentrada(String emailReentrada) {
        this.emailReentrada = emailReentrada;
    }

    public boolean isIcProcedenteReentrada() {
        return icProcedenteReentrada;
    }

    public void setIcProcedenteReentrada(boolean icProcedenteReentrada) {
        this.icProcedenteReentrada = icProcedenteReentrada;
    }

    public String getNomeRetirante() {
        return nomeRetirante;
    }

    public void setNomeRetirante(String nomeRetirante) {
        this.nomeRetirante = nomeRetirante;
    }

    public String getCpfRetirante() {
        return cpfRetirante;
    }

    public void setCpfRetirante(String cpfRetirante) {
        this.cpfRetirante = cpfRetirante;
    }

    public LocalDateTime getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDateTime dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public String getNomeParte() {
        return nomeParte;
    }

    public void setNomeParte(String nomeParte) {
        this.nomeParte = nomeParte;
    }

    public String getDocumentoParte() {
        return documentoParte;
    }

    public void setDocumentoParte(String documentoParte) {
        this.documentoParte = documentoParte;
    }

    public String getRequerimento() {
        return requerimento;
    }

    public void setRequerimento(String requerimento) {
        this.requerimento = requerimento;
    }

    public String getNrOrdem() {
        return nrOrdem;
    }

    public void setNrOrdem(String nrOrdem) {
        this.nrOrdem = nrOrdem;
    }

    public LocalDateTime getDataCancelamentoJudicial() {
        return dataCancelamentoJudicial;
    }

    public void setDataCancelamentoJudicial(LocalDateTime dataCancelamentoJudicial) {
        this.dataCancelamentoJudicial = dataCancelamentoJudicial;
    }

    public String getNrProcessoDuvida() {
        return nrProcessoDuvida;
    }

    public void setNrProcessoDuvida(String nrProcessoDuvida) {
        this.nrProcessoDuvida = nrProcessoDuvida;
    }


    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
