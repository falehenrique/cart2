package br.com.exmart.rtdpjlite.model.portal;

import br.com.exmart.rtdpjlite.model.AbstractEntity;
import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.listener.PedidoListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name="tb_pedido",  schema = "db_portal")

@EntityListeners(PedidoListener.class)
public class Pedido extends AbstractEntity {
    @OneToOne
    @NotNull
    private Cliente cliente;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime data;
    private String arquivo;
    private Long protocolo;
    private Long protocoloId;
    private String registro;
    @Column(name="numero_registro")
    private Long numeroRegistro;
    @OneToOne
    @JoinColumn(name="responsavel_id")
    private UsuarioPortal responsavel;

    private String subNatureza;
    private String observacao;
    private String tipo;
    @Column(name = "dt_finalizado")
    private LocalDateTime dtFinalizado;
    @Column(name = "nr_contrato")
    private String nrContrato;

    @NotEmpty
    private String parte;
    @NotEmpty
    private String parteDocumento;

//    @Basic(fetch=FetchType.LAZY)
//    @Formula(value="(select tipo_mensagem.nome from db_portal.tb_mensagem as mensagem,db_portal.tb_tipo_mensagem as tipo_mensagem where mensagem.tipo_id = tipo_mensagem.id and mensagem.pedido_id=id order by mensagem.data desc limit 1)")
//    private String ultimoStatus;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name="pedido_id")
    private Set<PedidoProtocolo> protocolos;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name="pedido_id")
    @OrderBy("nrOrdem ASC")
    private Set<PedidoStatus> statusPedido;

    @NotEmpty
    @Column(name = "status_atual_cliente")
    private String statusAtualCliente = StatusAtualCliente.AGUARDE_CARTORIO.getNome();

    @NotEmpty
    @Column(name = "status_atual_cartorio")
    private String statusAtualCartorio = StatusAtualCartorio.INTERAGIR.getNome();


    @NotNull
    private LocalDateTime ultimaAtualizacao = LocalDateTime.now();

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getData() {
        return data;
    }
    public void setData(LocalDateTime data) {
        this.data = data;
    }
    public String getArquivo() {
        return arquivo;
    }
    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }
    public Long getProtocolo() {
        return protocolo;
    }
    public void setProtocolo(Long protocolo) {
        this.protocolo = protocolo;
    }
    public String getObservacao() {
        return observacao;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public Long getNumeroRegistro() {
        return numeroRegistro;
    }
    public void setNumeroRegistro(Long numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }
    public String getRegistro() {
        return registro;
    }
    public void setRegistro(String registro) {
        this.registro = registro;
    }
    @Override
    public String toString() {
        return "Pedido [cliente=" + cliente+ ", data=" + data + ", arquivo=" + arquivo + ", protocolo="
                + protocolo + ", registro=" + registro + ", numeroRegistro=" + numeroRegistro + ", observacao="
                + observacao + ", tipo=" + tipo;
    }
    public String getSubNatureza() {
        return subNatureza;
    }
    public void setSubNatureza(String subNatureza) {
        this.subNatureza = subNatureza;
    }


//    @JsonIgnore
//    public Mensagem getUltimaMensagem(){
//        Mensagem retorno = null;
//        if (this.mensagem != null){
//            for(Mensagem mensagem : this.mensagem){
//                if(retorno == null){
//                    retorno = mensagem;
//                }
//                if (mensagem.getData().isAfter(retorno.getData())) {
//                    retorno = mensagem;
//                }
//            }
//        }
//        return retorno;
//    }



    public LocalDateTime getDtFinalizado() {
        return dtFinalizado;
    }

    public void setDtFinalizado(LocalDateTime dtFinalizado) {
        this.dtFinalizado = dtFinalizado;
    }

    public UsuarioPortal getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(UsuarioPortal responsavel) {
        this.responsavel = responsavel;
    }

    public Set<PedidoProtocolo> getProtocolos() {
        if(this.protocolos == null){
            this.protocolos = new HashSet<>();
        }
        return protocolos;
    }

    public void setProtocolos(Set<PedidoProtocolo> protocolos) {
        this.protocolos = protocolos;
    }

    public Long getProtocoloId() {
        return protocoloId;
    }

    public void setProtocoloId(Long protocoloId) {
        this.protocoloId = protocoloId;
    }

    public Set<PedidoStatus> getStatusPedido() {
        if(this.statusPedido == null)
            this.statusPedido = new HashSet<>();
        return statusPedido;
    }

    public void setStatusPedido(Set<PedidoStatus> statusPedido) {
        this.statusPedido = statusPedido;
    }

    public String getNrContrato() {
        return nrContrato;
    }

    public void setNrContrato(String nrContrato) {
        this.nrContrato = nrContrato;
    }

    public String getStatusAtualCliente() {
        return statusAtualCliente;
    }

    public void setStatusAtualCliente(String statusAtualCliente) {
        this.statusAtualCliente = statusAtualCliente;
    }


    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
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

    public String getStatusAtualCartorio() {
        return statusAtualCartorio;
    }

    public void setStatusAtualCartorio(String statusAtualCartorio) {
        this.statusAtualCartorio = statusAtualCartorio;
    }

    public enum StatusAtualCliente{
        AGUARDE_CARTORIO("AGUARDE O CARTÓRIO"),
        RESPONDER_CARTORIO("RESPONDER O CARTÓRIO"),
        PEDIDO_CONCLUIDO("PEDIDO CONCLUÍDO"),
        ;

        StatusAtualCliente(String nome) {
            this.nome = nome;
        }

        private String nome;

        public String getNome() {
            return nome;
        }
    }
    public enum StatusAtualCartorio{
        AGUARDE("AGUARDE"),
        INTERAGIR("INTERAGIR")
        ;

        StatusAtualCartorio(String nome) {
            this.nome = nome;
        }

        private String nome;

        public String getNome() {
            return nome;
        }
    }
}
