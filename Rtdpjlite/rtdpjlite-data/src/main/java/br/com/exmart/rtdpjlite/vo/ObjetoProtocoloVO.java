package br.com.exmart.rtdpjlite.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjetoProtocoloVO implements Serializable, Cloneable{
    private Long id;
    private String nome;
    private boolean icGarantia;
    private List<ObjetoProtocoloAtributoVO> atributos = new ArrayList<>();
    private List<ObjetoProtocoloParteVO> partes = new ArrayList<>();


    public ObjetoProtocoloVO(Long id, String nome, List<ObjetoProtocoloParteVO> partes, boolean icGarantia) {
        this.id = id;
        this.nome = nome != null? nome.toUpperCase():nome;
        this.partes = partes;
        this.icGarantia = icGarantia;
    }

    public ObjetoProtocoloVO() {
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

    public List<ObjetoProtocoloAtributoVO> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<ObjetoProtocoloAtributoVO> atributos) {
        this.atributos = atributos;
    }

    public List<ObjetoProtocoloParteVO> getPartes() {
        return partes;
    }

    public void setPartes(List<ObjetoProtocoloParteVO> partes) {
        this.partes = partes;
    }

    public boolean isIcGarantia() {
        return icGarantia;
    }

    public void setIcGarantia(boolean icGarantia) {
        this.icGarantia = icGarantia;
    }
}
