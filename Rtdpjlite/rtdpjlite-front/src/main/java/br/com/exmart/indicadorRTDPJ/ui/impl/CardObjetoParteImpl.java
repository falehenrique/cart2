package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.CardObjetoParte;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.rtdpjlite.interfaces.ParteInterface;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloParteVO;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import org.vaadin.spring.events.EventBus;
import org.vaadin.textfieldformatter.NumeralFieldFormatter;

import java.util.Set;


public class CardObjetoParteImpl extends CardObjetoParte{
    private Set<ParteInterface> partes;
    private ObjetoProtocoloParteVO parte;
    private CardObjetoImpl card;
    private boolean removerAtributo = false;
    private EventBus.ViewEventBus eventBus;

    public CardObjetoParteImpl(CardObjetoImpl card, Set<ParteInterface> partes, ObjetoProtocoloParteVO parte, EventBus.ViewEventBus eventBus) {
        this.partes = partes;
        this.card = card;
        this.eventBus = eventBus;
        new NumeralFieldFormatter(valor, ".", ",", 2);
        btnAddRemoveAtributo.addClickListener(evt->addRemoveAtributoClickListener(evt));
        parteCombo.setItemCaptionGenerator(parteProtocolo -> {return parteProtocolo.getNome();});
        parteCombo.setItems(this.partes);
        if(parte != null){
            for(ParteInterface parteProtocolo : partes){
                if(parteProtocolo.getCpfCnpj().equals(parte.getDocumento())){
                    parteCombo.setValue(parteProtocolo);
                }
            }
            valor.setValue(parte.getFracao().toString().replace(".",","));
            btnAddRemoveAtributo.setIcon(VaadinIcons.MINUS_CIRCLE_O);
            removerAtributo = !removerAtributo;
            btnAddRemoveAtributo.removeStyleName("green");
            btnAddRemoveAtributo.addStyleName("red");
            this.removeStyleName("well");
            this.addStyleName("card");
        }else{
            this.removeStyleName("card");
            this.addStyleName("well");
        }
        parteCombo.focus();
    }

    public ObjetoProtocoloParteVO getParte() {
        ObjetoProtocoloParteVO retorno = new ObjetoProtocoloParteVO();
        if(parteCombo.getValue() == null || Strings.isNullOrEmpty(valor.getValue())){
            return null;
        }
        retorno.setDocumento(parteCombo.getValue().getCpfCnpj());
        retorno.setFracao(new Float(valor.getValue().replace(",",".")));
        return retorno;
    }

    public void habilitaDesabilita(boolean habilitar) {
        btnAddRemoveAtributo.setVisible(habilitar);
        parteCombo.setEnabled(habilitar);
        valor.setEnabled(habilitar);
        if(parteCombo.getValue() == null && Strings.isNullOrEmpty(valor.getValue())) {
            this.setVisible(habilitar);
        }
    }

    private void addRemoveAtributoClickListener(Button.ClickEvent evt) {
        if(!removerAtributo) {
            if(parteCombo.getValue() != null && !Strings.isNullOrEmpty(valor.getValue())) {
                this.eventBus.publish(this, new ProtocoloEvent.ObjetoParteAdicionado(this.card));
                btnAddRemoveAtributo.setIcon(VaadinIcons.MINUS_CIRCLE_O);
                btnAddRemoveAtributo.removeStyleName("green");
                btnAddRemoveAtributo.addStyleName("red");
                this.removeStyleName("well");
                this.addStyleName("card");
                removerAtributo = !removerAtributo;
            }else{
                Notification.show("Ops ;) Acho que faltou preencher algum campo", Notification.Type.ERROR_MESSAGE);
            }
        }else{
            this.eventBus.publish(this, new ProtocoloEvent.ObjetoParteRemovido(this.card,this));
            btnAddRemoveAtributo.setIcon(VaadinIcons.PLUS_CIRCLE_O);
            btnAddRemoveAtributo.removeStyleName("red");
            btnAddRemoveAtributo.addStyleName("green");
            this.removeStyleName("card");
            this.addStyleName("well");
        }

    }
}
