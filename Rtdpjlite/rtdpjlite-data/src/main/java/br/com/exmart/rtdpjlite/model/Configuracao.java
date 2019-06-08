package br.com.exmart.rtdpjlite.model;

import com.google.common.base.Strings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracao", schema = "rtdpj")
public class Configuracao extends AbstractEntity{
    @Column(name = "diretorio_upload")
    private String diretorioUpload;
    @Column(name = "diretorio_upload_lote")
    private String diretorioUploadLote;
    @Column(name = "site_assinatura_digital")
    private String siteAssinaturaDigital;

    @Column(name = "nome_cartorio")
    private String nomeCartorio;
    @Column(name = "cnpj_cartorio")
    private String cnpjCartorio;
    @Column(name = "codigo_cns_cartorio")
    private String codigoCnsCartorio;

    @Column(name = "cep_cartorio")
    private String cepCartorio;
    @Column(name = "cidade_cartorio")
    private String cidadeCartorio;
    @Column(name = "sg_cartorio")
    private String sgCartorio;
    @Column(name = "bairro_cartorio")
    private String bairroCartorio;
    @Column(name = "logradouro_cartorio")
    private String logradouroCartorio;
    @Column(name = "nr_telefone_cartorio")
    private String nrTelefoneCartorio;
    @Column(name = "ds_atendimento_cartorio")
    private String dsAtendimentoCartorio;
    @Column(name = "ds_mensagem_carimbo_selo")
    private String dsMensagemCarimboSelo;

    @Column(name = "numero_logradouro_cartorio")
    private String numeroLogradouroCartorio;
    @Column(name = "complemento_logradouro_cartorio")
    private String complementoLogradouroCartorio;

    @Column(name = "carimbo_certidao_sem_registro")
    private String txtCarimboCertidaoSemRegistro;
    @Column(name = "texto_explicativo_assinatura")
    private String txtExplicativoAssinatura;
    @Column(name = "url_qrcode")
    private String urlQrcode;
    @Column(name = "oficial")
    private String oficial;
    @Column(name = "dt_ultima_consulta_indisponibilidade")
    private LocalDateTime dtUltimaConsultaIndisponibilidade;
    @Column(name = "ic_selo_eletronico")
    private Boolean icSeloEletronico;

    @Column(name = "certidao_automatica_id")
    private Long certidaoAutomaticaId;

    @Column(name = "ds_intimacao_comparecimento")
    private String dsIntimacaoComparecimento;
    @Column(name = "ds_intimacao_checklist")
    private String dsIntimacaoChecklist;

    @Column(name = "modelo_certidao_portal")
    private String modeloCertidaoPortal;
    @Column(name = "url_portal")
    private String urlPortal;

    public String getDiretorioUpload() {
        return diretorioUpload;
    }

    public void setDiretorioUpload(String diretorioUpload) {
        this.diretorioUpload = diretorioUpload;
    }

    public String getNomeCartorio() {
        return nomeCartorio;
    }

    public void setNomeCartorio(String nomeCartorio) {
        this.nomeCartorio = nomeCartorio;
    }

    public String getCnpjCartorio() {
        return cnpjCartorio;
    }

    public void setCnpjCartorio(String cnpjCartorio) {
        this.cnpjCartorio = cnpjCartorio;
    }


    public String getCepCartorio() {
        return cepCartorio;
    }

    public void setCepCartorio(String cepCartorio) {
        this.cepCartorio = cepCartorio;
    }

    public String getCidadeCartorio() {
        return cidadeCartorio;
    }

    public void setCidadeCartorio(String cidadeCartorio) {
        this.cidadeCartorio = cidadeCartorio;
    }

    public String getSgCartorio() {
        return sgCartorio;
    }

    public void setSgCartorio(String sgCartorio) {
        this.sgCartorio = sgCartorio;
    }

    public String getBairroCartorio() {
        return bairroCartorio;
    }

    public void setBairroCartorio(String bairroCartorio) {
        this.bairroCartorio = bairroCartorio;
    }

    public String getLogradouroCartorio() {
        return logradouroCartorio;
    }

    public void setLogradouroCartorio(String logradouroCartorio) {
        this.logradouroCartorio = logradouroCartorio;
    }

    public String getNumeroLogradouroCartorio() {
        return numeroLogradouroCartorio;
    }

    public void setNumeroLogradouroCartorio(String numeroLogradouroCartorio) {
        this.numeroLogradouroCartorio = numeroLogradouroCartorio;
    }

    public String getComplementoLogradouroCartorio() {
        return complementoLogradouroCartorio;
    }

    public void setComplementoLogradouroCartorio(String complementoLogradouroCartorio) {
        this.complementoLogradouroCartorio = complementoLogradouroCartorio;
    }
    public String getNrTelefoneCartorio() {
        return nrTelefoneCartorio;
    }
    public void setNrTelefoneCartorio(String nrTelefoneCartorio) {
        this.nrTelefoneCartorio = nrTelefoneCartorio;
    }

    public String getDsAtendimentoCartorio() {
        return dsAtendimentoCartorio;
    }

    public void setDsAtendimentoCartorio(String dsAtendimentoCartorio) {
        this.dsAtendimentoCartorio = dsAtendimentoCartorio;
    }
    public String getEnderecoCompleto() {
        StringBuffer retorno = new StringBuffer("CEP: "+ this.cepCartorio);
        retorno.append(" " + this.logradouroCartorio);
        retorno.append(", " + this.numeroLogradouroCartorio);
        if(!Strings.isNullOrEmpty(this.complementoLogradouroCartorio)){
            retorno.append(" - " + this.complementoLogradouroCartorio);
        }
        retorno.append(" - " + this.bairroCartorio);
        retorno.append(" - " + this.cidadeCartorio);
        retorno.append(" / " + this.sgCartorio);
        return  retorno.toString();
    }


    public String getTxtExplicativoAssinatura() {
        return txtExplicativoAssinatura;
    }

    public void setTxtExplicativoAssinatura(String txtExplicativoAssinatura) {
        this.txtExplicativoAssinatura = txtExplicativoAssinatura;
    }

    public String getUrlQrcode() {
        return urlQrcode;
    }

    public void setUrlQrcode(String urlQrcode) {
        this.urlQrcode = urlQrcode;
    }

    public String getTxtCarimboCertidaoSemRegistro() {
        return txtCarimboCertidaoSemRegistro;
    }

    public void setTxtCarimboCertidaoSemRegistro(String txtCarimboCertidaoSemRegistro) {
        this.txtCarimboCertidaoSemRegistro = txtCarimboCertidaoSemRegistro;
    }

    public String getCodigoCnsCartorio() {
        return codigoCnsCartorio;
    }

    public void setCodigoCnsCartorio(String codigoCnsCartorio) {
        this.codigoCnsCartorio = codigoCnsCartorio;
    }

    public String getDiretorioUploadLote() {
        return diretorioUploadLote;
    }

    public void setDiretorioUploadLote(String diretorioUploadLote) {
        this.diretorioUploadLote = diretorioUploadLote;
    }

    public String getOficial() {
        return oficial;
    }

    public void setOficial(String oficial) {
        this.oficial = oficial;
    }

    public LocalDateTime getDtUltimaConsultaIndisponibilidade() {
        return dtUltimaConsultaIndisponibilidade;
    }

    public void setDtUltimaConsultaIndisponibilidade(LocalDateTime dtUltimaConsultaIndisponibilidade) {
        this.dtUltimaConsultaIndisponibilidade = dtUltimaConsultaIndisponibilidade;
    }


    public String getDsMensagemCarimboSelo() {
        return dsMensagemCarimboSelo;
    }

    public void setDsMensagemCarimboSelo(String dsMensagemCarimboSelo) {
        this.dsMensagemCarimboSelo = dsMensagemCarimboSelo;
    }

    public Boolean getIcSeloEletronico() {
        return icSeloEletronico;
    }

    public void setIcSeloEletronico(Boolean icSeloEletronico) {
        this.icSeloEletronico = icSeloEletronico;
    }

    public Long getCertidaoAutomaticaId() {
        return certidaoAutomaticaId;
    }

    public void setCertidaoAutomaticaId(Long certidaoAutomaticaId) {
        this.certidaoAutomaticaId = certidaoAutomaticaId;
    }

    public String getDsIntimacaoComparecimento() {
        return dsIntimacaoComparecimento;
    }

    public void setDsIntimacaoComparecimento(String dsIntimacaoComparecimento) {
        this.dsIntimacaoComparecimento = dsIntimacaoComparecimento;
    }

    public String getDsIntimacaoChecklist() {
        return dsIntimacaoChecklist;
    }

    public void setDsIntimacaoChecklist(String dsIntimacaoChecklist) {
        this.dsIntimacaoChecklist = dsIntimacaoChecklist;
    }

    public String getSiteAssinaturaDigital() {
        return siteAssinaturaDigital;
    }

    public void setSiteAssinaturaDigital(String siteAssinaturaDigital) {
        this.siteAssinaturaDigital = siteAssinaturaDigital;
    }

    public String getModeloCertidaoPortal() {
        return modeloCertidaoPortal;
    }

    public void setModeloCertidaoPortal(String modeloCertidaoPortal) {
        this.modeloCertidaoPortal = modeloCertidaoPortal;
    }

    public String getUrlPortal() {
        return urlPortal;
    }

    public void setUrlPortal(String urlPortal) {
        this.urlPortal = urlPortal;
    }
}
