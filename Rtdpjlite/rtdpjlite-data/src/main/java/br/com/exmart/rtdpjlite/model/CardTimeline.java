package br.com.exmart.rtdpjlite.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(schema = "rtdpj")
public class CardTimeline {
    @Id
    private String registro;
    @Column(name = "numero_registro")
    private Long numeroRegistro;
    @Column(name = "data_registro")
    private LocalDateTime dataRegistro;
    @Column(name = "registro_referencia")
    private String registroReferencia;
    @Column(name = "situacao_atual")
    private String situacaoAtual;
    private String observacao;
    private Long protocolo;
    @Column(name = "especialidade")
    private String especialidade;
    private String natureza;

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public Long getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(Long numero_registro) {
        this.numeroRegistro = numero_registro;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getRegistroReferencia() {
        return registroReferencia;
    }

    public void setRegistroReferencia(String registroReferencia) {
        this.registroReferencia = registroReferencia;
    }

    public String getSituacaoAtual() {
        return situacaoAtual;
    }

    public void setSituacaoAtual(String situacaoAtual) {
        this.situacaoAtual = situacaoAtual;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Long protocolo) {
        this.protocolo = protocolo;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

}
