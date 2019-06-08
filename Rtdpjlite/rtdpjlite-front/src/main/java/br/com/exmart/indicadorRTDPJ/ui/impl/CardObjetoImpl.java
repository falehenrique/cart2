package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.CardObjeto;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.rtdpjlite.interfaces.ParteInterface;
import br.com.exmart.rtdpjlite.model.Objeto;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloAtributoVO;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloParteVO;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloVO;
import br.com.exmart.util.BeanLocator;
import com.vaadin.ui.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CardObjetoImpl extends CardObjeto {
    private EventBus.ViewEventBus eventBus;
    private Objeto objeto;
    private Protocolo protocolo;
    private ObjetoProtocoloVO objetoParte;
    private Set<ParteInterface> partes;
    protected static Logger logger= LoggerFactory.getLogger(CardObjetoImpl.class);
    boolean icGarantia= false;

    public CardObjetoImpl(Set<? extends  ParteInterface> partes, Objeto objeto, ObjetoProtocoloVO objetoVO, EventBus.ViewEventBus eventBus) {
        this.partes = (Set<ParteInterface>) partes;
        this.eventBus = eventBus;
        this.protocolo = protocolo;
        this.objeto = objeto;
        titulo.setValue(this.objeto.getNome());
        btnRemoverObjeto.addClickListener(evt-> removerObjeto(evt));
        btnEditarObjeto.addClickListener(evt->btnEditarObjetoListener(evt));
        btnSalvarObjeto.addClickListener(evt->btnSalvarObjetoListener(evt));
        atributosList.addComponent(new CardObjetoAtributoImpl(this, this.objeto.getAtributos(), null, this.eventBus));
        partesList.addComponent(new CardObjetoParteImpl(this, this.partes, null, this.eventBus));
        if(objetoVO != null) {
            for(ObjetoProtocoloAtributoVO objetoProtocoloAtributoVO : objetoVO.getAtributos()) {
                atributosList.addComponent(new CardObjetoAtributoImpl(this, this.objeto.getAtributos(), objetoProtocoloAtributoVO, this.eventBus));
            }
            this.icGarantia = objetoVO.isIcGarantia();
            partesList.setVisible(objetoVO.isIcGarantia());
            if(objetoVO.isIcGarantia()) {
                if (objetoVO.getPartes() != null) {
                    for (ObjetoProtocoloParteVO parte : objetoVO.getPartes()) {
                        partesList.addComponent(new CardObjetoParteImpl(this, this.partes, parte, this.eventBus));
                    }
                }
            }
            btnEditarObjeto.setEnabled(true);
        }else{
            partesList.setVisible(objeto.isIcGarantia());
            btnEditarObjeto.setEnabled(false);
        }

        btnGarantia.setVisible(objeto.isIcGarantia());

        if(this.eventBus != null)
            this.eventBus.subscribe(this);


        btnGarantia.addClickListener(evt -> habilitarGarantia());

        try {
            ((CardObjetoAtributoImpl) atributosList.getComponent(0)).focus();
        }catch (Exception e){

        }
    }

    private void btnSalvarObjetoListener(Button.ClickEvent evt) {
        habilitaDesabilita(false, true);
        this.eventBus.publish(this, new ProtocoloEvent.ObjetoAdicionado());
    }

    private void btnEditarObjetoListener(Button.ClickEvent evt) {
        habilitaDesabilita(true, true);
    }


    private void habilitarGarantia() {
        btnGarantia.setStyleName("small danger");
        partesList.setVisible(!partesList.isVisible());
    }

    private void removerObjeto(Button.ClickEvent evt) {
        this.eventBus.publish(this, new ProtocoloEvent.ObjetoRemovido(this));
    }


    @EventBusListenerMethod(scope = EventScope.VIEW)
    public void objetoAtributoRemovido(ProtocoloEvent.ObjetoAtributoRemovido evento){
        if(evento.getCardObjeto().equals(this)) {
            atributosList.removeComponent(evento.getAtributo());
        }
    }
    @EventBusListenerMethod(scope = EventScope.VIEW)
    public void objetoAtributoAdicionado(ProtocoloEvent.ObjetoAtributoAdicionado evento){
        if(evento.getCardObjeto().equals(this)) {
            atributosList.addComponent(new CardObjetoAtributoImpl(this, this.objeto.getAtributos(), null, this.eventBus), 0);
        }
    }
    public void habilitaDesabilita(boolean habilitar,boolean icFromProtocolo){
        atributosList.forEach(item -> {((CardObjetoAtributoImpl) item).habilitaDesabilita(habilitar);});
        btnRemoverObjeto.setVisible(habilitar);
        if(partesList.isVisible())
            partesList.forEach(item -> {((CardObjetoParteImpl) item).habilitaDesabilita(habilitar);});
        btnSalvarObjeto.setVisible(habilitar);
        btnEditarObjeto.setEnabled(true);
        btnEditarObjeto.setVisible(icFromProtocolo);
        if(!habilitar){
            if(partesList.getComponentCount() <=1){
                partesList.setVisible(false);
            }
        }else{
            if(icGarantia)
                partesList.setVisible(true);
        }

    }


    public ObjetoProtocoloVO getObjeto() {

        ObjetoProtocoloVO retorno = new ObjetoProtocoloVO(this.objeto.getId(), this.objeto.getNome(), this.getPartes(), this.objeto.isIcGarantia());

        atributosList.forEach(item -> {
            ObjetoProtocoloAtributoVO atributo = ((CardObjetoAtributoImpl) item).getAtribuo();
            if (atributo != null)
                retorno.getAtributos().add(atributo);
        });
        return retorno;
    }

    public List<ObjetoProtocoloParteVO> getPartes() {
        List<ObjetoProtocoloParteVO> retorno = new ArrayList<>();
        partesList.forEach(item ->{
            ObjetoProtocoloParteVO parte = ((CardObjetoParteImpl) item).getParte();
            if(parte != null){
                retorno.add(parte);
            }
        });

        return retorno;
    }



    @EventBusListenerMethod(scope = EventScope.VIEW)
    public void objetoParteRemovido(ProtocoloEvent.ObjetoParteRemovido evento){
        logger.info("objetoParteRemovido");
        if(evento.getCardObjeto().equals(this)) {
            partesList.removeComponent(evento.getParte());
        }
    }
    @EventBusListenerMethod(scope = EventScope.VIEW)
    public void objetoAtributoAdicionado(ProtocoloEvent.ObjetoParteAdicionado evento){
        logger.info("objetoAtributoAdicionado");
        if(evento.getCardObjeto().equals(this)) {
            partesList.addComponent(new CardObjetoParteImpl(this, this.partes, null, this.eventBus), 0);
        }
    }
}

