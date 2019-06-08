package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.ModalProtocoloBasico;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.rtdpjlite.model.Natureza;
import br.com.exmart.rtdpjlite.model.SubNatureza;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.NaturezaService;
import br.com.exmart.util.BeanLocator;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Window;
import org.vaadin.spring.events.EventBus;

public class ModalProtocoloBasicoImpl extends ModalProtocoloBasico {
    private NaturezaService naturezaService;
    private TipoProtocolo tipoProtocolo;
    private Window window;

    private EventBus.ViewEventBus eventBus;
    public ModalProtocoloBasicoImpl(EventBus.ViewEventBus eventBus, TipoProtocolo tipoProtocolo) {
        this.eventBus = eventBus;
        this.tipoProtocolo = tipoProtocolo;
        this.naturezaService = BeanLocator.find(NaturezaService.class);
        comboNatureza.setItems(naturezaService.findAll());
        comboNatureza.setItemCaptionGenerator(Natureza::getNome);
        comboSubNatureza.setItemCaptionGenerator(SubNatureza::getNome);
        comboNatureza.addValueChangeListener(evt->comboNaturezaVlaueChangeListener(evt));

        btnProtocolar.addClickListener(evt->btnProtocolarClickListener());
        comboNatureza.setEmptySelectionAllowed(false);
        comboSubNatureza.setEmptySelectionAllowed(false);
        comboNatureza.focus();
        btnCancelar.addClickListener(evt->{this.window.close();});
    }

    private void btnProtocolarClickListener() {
        eventBus.publish(this, new ProtocoloEvent.ProtocolarLote(this.tipoProtocolo, comboNatureza.getValue(), comboSubNatureza.getValue()));
        this.window.close();
    }

    private void comboNaturezaVlaueChangeListener(HasValue.ValueChangeEvent<Natureza> evt) {
        if(evt.getValue() != null){
            comboSubNatureza.setItems(evt.getValue().getSubNaturezas());
        }
    }
}
