package br.com.exmart.rtdpjlite.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(schema = "rtdpj")
public class ProtocoloGrid {
    @Id
    private Long id;
    private Long numero;
    private String natureza;
    private Long tipo;
    private String apresentante;
    private LocalDate dataVencimento;
    private String parte;
    private String status;

    public ProtocoloGrid() {
    }

    public ProtocoloGrid(Long id, Long numero, String natureza, Long tipo, String apresentante, LocalDate dataVencimento, String parte, String status) {
        this.id = id;
        this.numero = numero;
        this.natureza = natureza;
        this.tipo = tipo;
        this.apresentante = apresentante;
        this.dataVencimento = dataVencimento;
        this.parte = parte;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public String getApresentante() {
        return apresentante;
    }

    public void setApresentante(String apresentante) {
        this.apresentante = apresentante;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getParte() {
        return parte;
    }

    public void setParte(String parte) {
        this.parte = parte;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
