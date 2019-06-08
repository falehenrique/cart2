package br.com.exmart.rtdpjlite.model;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity()
@Table(name = "intimacao_protocolo", schema = "rtdpj")
public class IntimacaoProtocolo extends AbstractEntity {
    @Column
    @NotEmpty
    private String nome;
    @NotEmpty
    @Column(length=10485760)
    private String resultado;
    private LocalDateTime dia = LocalDateTime.now();


    public IntimacaoProtocolo() {
    }

    public IntimacaoProtocolo(String nome, String resultado, LocalDateTime dia) {
        this.nome = nome;
        this.resultado = resultado;
        this.dia = dia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public LocalDateTime getDia() {
        return dia;
    }

    public void setDia(LocalDateTime dia) {
        this.dia = dia;
    }
}
