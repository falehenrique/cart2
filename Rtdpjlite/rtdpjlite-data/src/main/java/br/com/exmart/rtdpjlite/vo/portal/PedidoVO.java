package br.com.exmart.rtdpjlite.vo.portal;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class PedidoVO {
    @Id
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime data;
    private String parte;
    private String parteDocumento;
    private String responsavelNome;
    private String apresentante;
    private String nrContrato;
    private String status;
    private Long numeroRegistro;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime ultimaAtualizacao;
    private Integer diasParado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getParte() {
        return parte;
    }

    public void setParte(String parte) {
        this.parte = parte;
    }

    public String getParteDocumento() {
        return parteDocumento;
    }

    public void setParteDocumento(String parteDocumento) {
        this.parteDocumento = parteDocumento;
    }

    public String getResponsavelNome() {
        return responsavelNome;
    }

    public void setResponsavelNome(String responsavelNome) {
        this.responsavelNome = responsavelNome;
    }

    public String getApresentante() {
        return apresentante;
    }

    public void setApresentante(String apresentante) {
        this.apresentante = apresentante;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public Integer getDiasParado() {
        return diasParado;
    }

    public void setDiasParado(Integer diasParado) {
        this.diasParado = diasParado;
    }

    public String getNrContrato() {
        return nrContrato;
    }

    public void setNrContrato(String nrContrato) {
        this.nrContrato = nrContrato;
    }

    public Long getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(Long numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }
}
