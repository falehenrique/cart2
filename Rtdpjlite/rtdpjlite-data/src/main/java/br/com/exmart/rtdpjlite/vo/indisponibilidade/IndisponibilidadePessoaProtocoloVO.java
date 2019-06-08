package br.com.exmart.rtdpjlite.vo.indisponibilidade;

import java.time.LocalDateTime;

public class IndisponibilidadePessoaProtocoloVO {
    private Integer cdCartorioReferencia;
    private Long protocoloId;
    private Long nrProtocolo;
    private LocalDateTime dtProtocolo;
    private String registroReferencia;


    public IndisponibilidadePessoaProtocoloVO(Integer cdCartorioReferencia, Long protocoloId, Long nrProtocolo, LocalDateTime dtProtocolo, String registroReferencia) {
        this.cdCartorioReferencia = cdCartorioReferencia;
        this.protocoloId = protocoloId;
        this.nrProtocolo = nrProtocolo;
        this.dtProtocolo = dtProtocolo;
        this.registroReferencia = registroReferencia;
    }

    public IndisponibilidadePessoaProtocoloVO() {
    }

    public Integer getCdCartorioReferencia() {
        return cdCartorioReferencia;
    }

    public void setCdCartorioReferencia(Integer cdCartorioReferencia) {
        this.cdCartorioReferencia = cdCartorioReferencia;
    }

    public Long getNrProtocolo() {
        return nrProtocolo;
    }

    public void setNrProtocolo(Long nrProtocolo) {
        this.nrProtocolo = nrProtocolo;
    }

    public LocalDateTime getDtProtocolo() {
        return dtProtocolo;
    }

    public void setDtProtocolo(LocalDateTime dtProtocolo) {
        this.dtProtocolo = dtProtocolo;
    }

    public String getRegistroReferencia() {
        return registroReferencia;
    }

    public void setRegistroReferencia(String registroReferencia) {
        this.registroReferencia = registroReferencia;
    }

    public Long getProtocoloId() {
        return protocoloId;
    }

    public void setProtocoloId(Long protocoloId) {
        this.protocoloId = protocoloId;
    }
}
