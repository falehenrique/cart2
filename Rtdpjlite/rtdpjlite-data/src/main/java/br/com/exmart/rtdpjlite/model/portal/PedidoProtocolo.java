package br.com.exmart.rtdpjlite.model.portal;

import br.com.exmart.rtdpjlite.model.AbstractEntity;
import br.com.exmart.rtdpjlite.model.CartorioParceiro;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(schema = "db_portal", name = "tb_pedido_protocolo")
public class PedidoProtocolo extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name="cartorio_parceiro_id")
    private CartorioParceiro cartorioParceiro;
    @ManyToOne
    @JoinColumn(name="protocolo_id")
    @JsonIgnore
    private Protocolo protocolo;
    @Column(name = "protocolo")
    private String protocoloParceiro;
    @Column(name = "protocoloData")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate protocoloParceiroData;
    @Column(name = "registro")
    private String registroParceiro;
    @Column(name = "registroData")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate registroParceiroData;
    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name="pedido_protocolo_id")
    @OrderBy("nrOrdem ASC")
    private Set<PedidoProtocoloStatus> status;
    @Column(name = "protocolo_averbacao_id")
    private Long protocoloAverbacaoId;

    @NotEmpty
    @Column(name = "status_atual_parceiro")
    private String statusAtualParceiro = PedidoService.StatusPedido.PROTOCOLO.getNomeAguardando();

    @NotNull
    private LocalDateTime ultimaAtualizacao = LocalDateTime.now();


    public Protocolo getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Protocolo protocolo) {
        this.protocolo = protocolo;
    }

    public String getProtocoloParceiro() {
        return protocoloParceiro;
    }

    public void setProtocoloParceiro(String protocoloParceiro) {
        this.protocoloParceiro = protocoloParceiro;
    }

    public LocalDate getProtocoloParceiroData() {
        return protocoloParceiroData;
    }

    public void setProtocoloParceiroData(LocalDate protocoloParceiroData) {
        this.protocoloParceiroData = protocoloParceiroData;
    }

    public String getRegistroParceiro() {
        return registroParceiro;
    }

    public void setRegistroParceiro(String registroParceiro) {
        this.registroParceiro = registroParceiro;
    }

    public LocalDate getRegistroParceiroData() {
        return registroParceiroData;
    }

    public void setRegistroParceiroData(LocalDate registroParceiroData) {
        this.registroParceiroData = registroParceiroData;
    }

    public Set<PedidoProtocoloStatus> getStatus() {
        if(this.status == null)
            this.status = new HashSet<>();
        return status;
    }

    public void setStatus(Set<PedidoProtocoloStatus> status) {
        this.status = status;
    }

    public CartorioParceiro getCartorioParceiro() {
        return cartorioParceiro;
    }

    public void setCartorioParceiro(CartorioParceiro cartorioParceiro) {
        this.cartorioParceiro = cartorioParceiro;
    }

    public Long getProtocoloAverbacaoId() {
        return protocoloAverbacaoId;
    }

    public void setProtocoloAverbacaoId(Long protocoloAverbacaoId) {
        this.protocoloAverbacaoId = protocoloAverbacaoId;
    }

    public String getStatusAtualParceiro() {
        return statusAtualParceiro;
    }

    public void setStatusAtualParceiro(String statusAtualParceiro) {
        this.statusAtualParceiro = statusAtualParceiro;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String  getProximoStatus() {
        for(PedidoProtocoloStatus status : this.getStatus()){
            if(status.getData() == null){
                return status.getNomeAguardando();
            }
        }
        return Pedido.StatusAtualCliente.PEDIDO_CONCLUIDO.getNome();
    }
}

