package br.com.exmart.rtdpjlite.vo.indisponibilidade;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

public class VerificadoVO {
    private Long idParte;
    private String tipoProtocolo;
    private Long protocolo;
    private LocalDateTime data;

    public VerificadoVO(Long idParte, String tipoProtocolo, Long protocolo, LocalDateTime data) {
        this.idParte = idParte;
        this.tipoProtocolo = tipoProtocolo;
        this.protocolo = protocolo;
        this.data = data;
    }

    public Long getIdParte() {
        return idParte;
    }

    public void setIdParte(Long idParte) {
        this.idParte = idParte;
    }

    public String getTipoProtocolo() {
        return tipoProtocolo;
    }

    public void setTipoProtocolo(String tipoProtocolo) {
        this.tipoProtocolo = tipoProtocolo;
    }

    public Long getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Long protocolo) {
        this.protocolo = protocolo;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
