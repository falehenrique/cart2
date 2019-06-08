package br.com.exmart.rtdpjlite.vo.balcaoonline;

public class PedidoMensagemRemetente {
    //POST NO /usuario/
    private Long id;
    private String nome;
    private String email;


    public PedidoMensagemRemetente(Long id) {
        this.id = id;
    }

    public PedidoMensagemRemetente(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public PedidoMensagemRemetente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
