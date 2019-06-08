package br.com.exmart.rtdpjlite.vo.seloeletronico;

import br.com.exmart.rtdpjlite.model.ParteProtocolo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SeloParteRTDPJ {
    private String nome;
    private String documento;


    public SeloParteRTDPJ(String nome, String documento) {
        this.nome = nome;
        this.documento = documento;
    }

    public SeloParteRTDPJ() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public static List<SeloParteRTDPJ> fromParteProtocolo(Set<ParteProtocolo> parteProtocolos){
        List<SeloParteRTDPJ> retorno = new ArrayList<>();
        for(ParteProtocolo parte : parteProtocolos){
            retorno.add(new SeloParteRTDPJ(parte.getParte().getNome(), parte.getParte().getCpfCnpj()));
        }
        return  retorno;
    }
}
