package br.com.exmart.indicadorRTDPJ.ui.custom.event;

import br.com.exmart.indicadorRTDPJ.ui.impl.CardObjetoAtributoImpl;
import br.com.exmart.indicadorRTDPJ.ui.impl.CardObjetoImpl;
import br.com.exmart.indicadorRTDPJ.ui.impl.CardObjetoParteImpl;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;

import java.io.Serializable;
import java.util.List;

public abstract class ProtocoloEvent implements Serializable {



    public static final class ProtocolarLote implements Serializable {
        private Natureza natureza;
        private SubNatureza subNatureza;
        private TipoProtocolo tipoProtocolo;

        public ProtocolarLote(TipoProtocolo tipoProtocolo, Natureza natureza, SubNatureza subnaturea) {
            this.natureza = natureza;
            this.subNatureza = subnaturea;
            this.tipoProtocolo = tipoProtocolo;
        }

        public Natureza getNatureza() {
            return natureza;
        }

        public void setNatureza(Natureza natureza) {
            this.natureza = natureza;
        }

        public SubNatureza getSubNatureza() {
            return subNatureza;
        }

        public void setSubNatureza(SubNatureza subNatureza) {
            this.subNatureza = subNatureza;
        }

        public TipoProtocolo getTipoProtocolo() {
            return tipoProtocolo;
        }
    }

    public static final class CarimboAtualizado implements Serializable{
        private String texto;

        public CarimboAtualizado(String texto) {
            this.texto = texto;
        }

        public String getTexto() {
            return texto;
        }
    }

    public static final class SalvarProtocolo implements Serializable{
        private Protocolo protocolo;

        public SalvarProtocolo(Protocolo protocolo) {
            this.protocolo = protocolo;
        }

        public Protocolo getProtocolo() {
            return protocolo;
        }
    }



    public static final class Reentrada implements Serializable{

    }

    public static final class ObjetoRemovido implements Serializable{
        private CardObjetoImpl objeto;

        public ObjetoRemovido(CardObjetoImpl objeto) {
            this.objeto = objeto;
        }

        public CardObjetoImpl getObjeto() {
            return objeto;
        }
    }
    public static final class ObjetoAdicionado implements Serializable{

    }

    public static final class ServicosAtualizado implements Serializable{
        List<ServicoCalculado> servicos;

        public ServicosAtualizado(List<ServicoCalculado> servicos) {
            this.servicos = servicos;
        }

        public List<ServicoCalculado> getServicos() {
            return servicos;
        }
    }
    public static final class ObjetoAtributoRemovido implements Serializable{
        private CardObjetoAtributoImpl atributo;
        private  CardObjetoImpl cardObjeto;
        public ObjetoAtributoRemovido(CardObjetoImpl card, CardObjetoAtributoImpl atributo) {
            this.atributo = atributo;
            this.cardObjeto = card;
        }

        public CardObjetoImpl getCardObjeto() {
            return cardObjeto;
        }

        public CardObjetoAtributoImpl getAtributo() {
            return atributo;
        }
    }
    public static final class ObjetoParteRemovido implements Serializable{
        private CardObjetoParteImpl parte;
        private  CardObjetoImpl cardObjeto;
        public ObjetoParteRemovido(CardObjetoImpl card, CardObjetoParteImpl parte) {
            this.parte = parte;
            this.cardObjeto = card;
        }

        public CardObjetoImpl getCardObjeto() {
            return cardObjeto;
        }

        public CardObjetoParteImpl getParte() {
            return parte;
        }
    }

    public static final class ObjetoAtributoAdicionado implements Serializable{

        private final CardObjetoImpl cardObjeto;

        public ObjetoAtributoAdicionado(CardObjetoImpl cardObjeto) {
            this.cardObjeto = cardObjeto;
        }

        public CardObjetoImpl getCardObjeto() {
            return cardObjeto;
        }
    }

    public static final class ObjetoParteAdicionado implements Serializable{

        private final CardObjetoImpl cardObjeto;

        public ObjetoParteAdicionado(CardObjetoImpl cardObjeto) {
            this.cardObjeto = cardObjeto;
        }

        public CardObjetoImpl getCardObjeto() {
            return cardObjeto;
        }
    }

    public static final class RetroativoCadastrado implements Serializable{
        Registro registro;

        public RetroativoCadastrado(Registro registro) {
            this.registro = registro;
        }

        public Registro getRegistro() {
            return registro;
        }
    }

    public static final class AptoRegistroProtocolo implements Serializable{
        Protocolo protocolo;
        StatusProtocoloJson conteudo;
        public AptoRegistroProtocolo(Protocolo protocolo, StatusProtocoloJson conteudo) {
            this.protocolo = protocolo;
            this.conteudo = conteudo;
        }

        public StatusProtocoloJson getConteudo() {
            return conteudo;
        }

        public Protocolo getProtocolo() {
            return protocolo;
        }
    }

    public static final class DevolverProtocolo implements Serializable{
        StatusProtocoloJson json;

        public DevolverProtocolo(StatusProtocoloJson json) {
            this.json = json;
        }

        public StatusProtocoloJson getJson() {
            return json;
        }
    }

    public static final class FinalizarProtocoloCertidao implements Serializable{
        StatusProtocoloJson json;

        public FinalizarProtocoloCertidao(StatusProtocoloJson json) {
            this.json = json;
        }

        public StatusProtocoloJson getJson() {
            return json;
        }
    }


    public static final class RegistrarProtocolo implements Serializable{
        StatusProtocoloJson json;
        Protocolo protocolo;
        public RegistrarProtocolo(Protocolo protocolo, StatusProtocoloJson json) {
            this.json = json;
            this.protocolo = protocolo;
        }

        public Protocolo getProtocolo() {
            return protocolo;
        }

        public StatusProtocoloJson getJson() {
            return json;
        }
    }

    public static final class FinalizarCertidaoIntimacao implements Serializable{
        StatusProtocoloJson json;
        Protocolo protocolo;
        public FinalizarCertidaoIntimacao(Protocolo protocolo, StatusProtocoloJson json) {
            this.json = json;
            this.protocolo = protocolo;
        }

        public Protocolo getProtocolo() {
            return protocolo;
        }

        public StatusProtocoloJson getJson() {
            return json;
        }
    }


    public static final class ProtocolarExameCalculo implements Serializable{
        private Protocolo protocolo;

        public ProtocolarExameCalculo(Protocolo protocolo) {
            this.protocolo = protocolo;
        }

        public Protocolo getProtocolo() {
            return protocolo;
        }
    }

    public static final class ParteIndisponivel implements Serializable{
        private boolean possui;
        public ParteIndisponivel(boolean possui) {
            this.possui = possui;
        }

        public boolean isPossui() {
            return possui;
        }
    }

}
