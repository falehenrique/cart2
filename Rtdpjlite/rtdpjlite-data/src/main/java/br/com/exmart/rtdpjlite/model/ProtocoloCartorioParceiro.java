package br.com.exmart.rtdpjlite.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity()
@Table(name = "protocolo_cartorio_parceiro", schema = "rtdpj")
public class ProtocoloCartorioParceiro extends AbstractEntity {
    @OneToOne
    CartorioParceiro cartorioParceiro;


    public CartorioParceiro getCartorioParceiro() {
        return cartorioParceiro;
    }

    public void setCartorioParceiro(CartorioParceiro cartorioParceiro) {
        this.cartorioParceiro = cartorioParceiro;
    }

    @Override
    public String toString() {
        return getCartorioParceiro().getNome();
    }

    public ProtocoloCartorioParceiro clone(){
        ProtocoloCartorioParceiro novo = new ProtocoloCartorioParceiro();
        novo.setCartorioParceiro(this.getCartorioParceiro());
        return novo;
    }

}
