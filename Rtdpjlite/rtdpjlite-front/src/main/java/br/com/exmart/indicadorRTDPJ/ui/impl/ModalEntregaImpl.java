package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.ModalEntrega;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.Status;
import br.com.exmart.rtdpjlite.model.StatusProtocolo;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.vaadin.spring.events.EventBus;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter;

import java.time.LocalDateTime;

public class ModalEntregaImpl extends ModalEntrega {
    private Window window;
    private ProtocoloService protocoloService;
    private Protocolo protocolo;
    private Status status;
    private BeanValidationBinder<StatusProtocoloJson> binder = new BeanValidationBinder<>(StatusProtocoloJson.class);
    private StatusProtocoloJson statusProtocoloGson = new StatusProtocoloJson();
    private EventBus.ViewEventBus eventBus;



    public ModalEntregaImpl(Protocolo protocolo, Status status) {
        this.protocolo = protocolo;
        this.status = status;
        this.protocoloService = BeanLocator.find(ProtocoloService.class);
        this.eventBus = BeanLocator.find(EventBus.ViewEventBus.class);
        new CustomStringBlockFormatter(documento, new int[]{3, 3, 3, 2}, new String[]{".", ".", "-"}, CustomStringBlockFormatter.ForceCase.NONE);
        documento.addBlurListener(evt -> validarDocumento(evt));
        btnCancelar.addClickListener(evt -> {window.close();});
        btnConfirmar.addClickListener(evt -> confirmarClickListener(evt));
        data.setRangeEnd(LocalDateTime.now());
        LocalDateTime dtInicial = protocolo.getData_protocolo();
        if(protocolo.getDtRegistro() != null){
            dtInicial = protocolo.getDtRegistro();
        } else {
            for(StatusProtocolo statusProtocolo : protocolo.getStatusProtocolo()){
                if(statusProtocolo.getStatus().getNome().equalsIgnoreCase( "DEVOLVIDO")){
                    dtInicial = statusProtocolo.getData();
                }
            }
        }

        data.setRangeStart(dtInicial);
        this.statusProtocoloGson.setDataRetirada(LocalDateTime.now());
        bindCampos();
        nome.focus();

    }

    private void bindCampos() {
        binder.bind(nome, "nomeRetirante");
        binder.bind(documento, "cpfRetirante");
        binder.bind(data, "dataRetirada");

        binder.setBean(this.statusProtocoloGson);
    }

    private void confirmarClickListener(Button.ClickEvent evt) {
        if(Strings.isNullOrEmpty(nome.getValue()) || Strings.isNullOrEmpty(documento.getValue()) || data.getValue() == null) {
            Notification.show("Ops ;) Acho que faltou preencher algum campo corretamente", Notification.Type.ERROR_MESSAGE);
        }else{
            this.protocolo.getStatusProtocolo().add(new StatusProtocolo(this.status, LocalDateTime.now(), null,binder.getBean(), ((MyUI) UI.getCurrent()).getUsuarioLogado()));
            //TODO deve salvar os dados refernte a esse status
            this.window.close();
            this.eventBus.publish(this, new ProtocoloEvent.SalvarProtocolo(this.protocolo));
        }
    }

    private void validarDocumento(FieldEvents.BlurEvent evt) {
//        System.out.println(FormatarDocumento.validarCpfCnpj(documento.getValue()));
    }
}
