package br.com.exmart.rtdpjlite.model;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lote", schema = "rtdpj")
public class Lote extends AbstractEntity {
    @OneToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;
    @OneToOne
    @JoinColumn(name="cliente_id")
    private Cliente cliente;

    @Basic(fetch=FetchType.LAZY)
    @Formula(value="(select count(*) from rtdpj.lote_item li where li.lote_id = id and li.finalizado is not null )")
    private Integer finalizados;

    @Basic(fetch=FetchType.LAZY)
    @Formula(value="(select count(*) from rtdpj.lote_item li where li.lote_id = id )")
    private Integer total;

    @Basic(fetch=FetchType.LAZY)
    @Formula(value="(select count(*) from rtdpj.lote_item li where li.lote_id = id and li.resultado is not null )")
    private Integer error;

    private LocalDateTime dia;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDia() {
        return dia;
    }

    public void setDia(LocalDateTime dia) {
        this.dia = dia;
    }

    public Integer getFinalizados() {
        return finalizados;
    }

    public void setFinalizados(Integer finalizados) {
        this.finalizados = finalizados;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }
}
