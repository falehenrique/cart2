package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.CardObjetoAtributo;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.rtdpjlite.model.ObjetoAtributo;
import br.com.exmart.rtdpjlite.model.ObjetoAtributoOpcao;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloAtributoVO;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import org.vaadin.spring.events.EventBus;

import java.util.List;

public class CardObjetoAtributoImpl extends CardObjetoAtributo {
    private EventBus.ViewEventBus eventBus;
    private boolean removerAtributo = false;
    private CardObjetoImpl card;
    private ObjetoProtocoloAtributoVO retorno;

    public CardObjetoAtributoImpl(CardObjetoImpl card, List<ObjetoAtributo> atributos, ObjetoProtocoloAtributoVO atributoVO, EventBus.ViewEventBus eventBus) {
        this.card = card;
        this.eventBus = eventBus;
        btnAddRemoveAtributo.addClickListener(evt->addRemoveAtributoClickListener(evt));
        atributo.setItems(atributos);
        valorCombo.setItemCaptionGenerator(ObjetoAtributoOpcao::getNome);
        atributo.addValueChangeListener(evt->atributoListener(evt));
        if(atributoVO != null){
            for (ObjetoAtributo attr : atributos) {
                if (attr.getNome().equalsIgnoreCase(atributoVO.getNome())){
                    atributo.setValue(attr);
                    if(attr.getOpcoes().size() > 0 ){
                        for(ObjetoAtributoOpcao opcao :attr.getOpcoes()){
                            if(opcao.getNome().equalsIgnoreCase(atributoVO.getValor())){
                                valorCombo.setValue(opcao);
                                valorCombo.setVisible(true);
                                valor.setVisible(false);
                            }
                        }
                    }else {
                        valor.setValue(atributoVO.getValor());
                    }
                }
            }
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
        atributo.focus();
    }

    public void focus(){
        atributo.focus();
    }

    private void atributoListener(HasValue.ValueChangeEvent<ObjetoAtributo> evt) {
        ObjetoAtributo selecionado = evt.getValue();
        if(selecionado.getOpcoes().size() > 0){
            valorCombo.clear();
            valorCombo.setItems(selecionado.getOpcoes());
            valorCombo.setVisible(true);
            valor.setVisible(false);
        }else{
            valorCombo.setVisible(false);
            valor.setVisible(true);
        }
    }

    private void addRemoveAtributoClickListener(Button.ClickEvent evt) {
        if(!removerAtributo) {
            if(atributo.getValue() != null && !Strings.isNullOrEmpty(recuperaValor())) {
                this.eventBus.publish(this, new ProtocoloEvent.ObjetoAtributoAdicionado(this.card));
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
            this.eventBus.publish(this, new ProtocoloEvent.ObjetoAtributoRemovido(this.card,this));
            btnAddRemoveAtributo.setIcon(VaadinIcons.PLUS_CIRCLE_O);
            btnAddRemoveAtributo.removeStyleName("red");
            btnAddRemoveAtributo.addStyleName("green");
            this.removeStyleName("card");
            this.addStyleName("well");
        }

    }

    public void habilitaDesabilita(boolean habilitar){
        btnAddRemoveAtributo.setVisible(habilitar);
        atributo.setEnabled(habilitar);
        valor.setEnabled(habilitar);
        valorCombo.setEnabled(habilitar);
        if(atributo.getValue() == null && Strings.isNullOrEmpty(recuperaValor())) {
            this.setVisible(habilitar);
        }
    }

    public ObjetoProtocoloAtributoVO getAtribuo() {
        if(atributo.getValue() != null && !Strings.isNullOrEmpty(recuperaValor())) {
            return new ObjetoProtocoloAtributoVO(atributo.getValue().getId(), atributo.getValue().getNome(), recuperaValor());
        }else{
            return null;
        }
    }

    private String recuperaValor() {
        if(valor.isVisible()){
            return valor.getValue();
        }else{
            return valorCombo.getValue().getNome();
        }
    }
}
