package br.com.exmart.rtdpjlite.model.portal;


import br.com.exmart.rtdpjlite.model.AbstractEntity;
import br.com.exmart.rtdpjlite.model.CartorioParceiro;
import br.com.exmart.rtdpjlite.model.Cliente;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_usuario",  schema = "db_portal")
public class UsuarioPortal extends AbstractEntity {

    @NotEmpty
    private String nome;
    private String usuario;
    @Email
    private String email;
    private String senha;
    private String resetToken;
    @OneToOne
    private Cliente cliente;
    @OneToOne
    @JoinColumn(name = "cartorio_parceiro_id")
    private CartorioParceiro cartorioParceiro;
    @Column(name = "lembrete_senha")
    @NotEmpty
    private String lembreteSenha;

    @Column(name="dt_cadastro")
    private LocalDateTime dtCadastro;

    public UsuarioPortal() {
    }
    public UsuarioPortal(Long id) {
        setId(id);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDateTime getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDateTime dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /*
        @Override
        public Long getId() {
            return super.getId();
        }

        @Override
        public void setId(Long id) {
            super.setId(id);
        }
    */
    @Override
    public String toString() {
        return nome;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String reset_token) {
        this.resetToken = reset_token;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public CartorioParceiro getCartorioParceiro() {
        return cartorioParceiro;
    }

    public void setCartorioParceiro(CartorioParceiro cartorioParceiro) {
        this.cartorioParceiro = cartorioParceiro;
    }

    public String getLembreteSenha() {
        return lembreteSenha;
    }

    public void setLembreteSenha(String lembreteSenha) {
        this.lembreteSenha = lembreteSenha;
    }

}
