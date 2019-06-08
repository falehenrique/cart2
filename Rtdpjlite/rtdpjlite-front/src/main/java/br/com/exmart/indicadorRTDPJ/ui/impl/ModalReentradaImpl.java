package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.ModalReentrada;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.Status;
import br.com.exmart.rtdpjlite.model.StatusProtocolo;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import br.com.exmart.util.BeanLocator;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.vaadin.spring.events.EventBus;

import java.time.LocalDateTime;

public class ModalReentradaImpl extends ModalReentrada{
	private Window window;
	private ProtocoloService protocoloService;
	private Protocolo protocolo;
	private Status status;
	private BeanValidationBinder<StatusProtocoloJson> binder = new BeanValidationBinder<>(StatusProtocoloJson.class);
	private StatusProtocoloJson statusProtocoloGson = new StatusProtocoloJson();
	private EventBus.ViewEventBus eventBus;


	public ModalReentradaImpl(Protocolo protocolo, Status status) {
		this.protocolo = protocolo;
		this.status = status;
		this.protocoloService = BeanLocator.find(ProtocoloService.class);
		this.eventBus = BeanLocator.find(EventBus.ViewEventBus.class);
//		new CustomStringBlockFormatter(documento, new int[]{3, 3, 3, 2}, new String[]{".", ".", "-"}, CustomStringBlockFormatter.ForceCase.NONE);
//		documento.addBlurListener(evt -> validarDocumento(evt));
		btnCancelar.addClickListener(evt -> {window.close();});
		btnConfirmar.addClickListener(evt -> confirmarClickListener(evt));

		bindCampos();

		apresentante.focus();
	}
	private void bindCampos() {
		binder.bind(apresentante, "apresentanteReentrada");
		binder.bind(apresentanteEmail, "emailReentrada");
		binder.bind(apresentanteTelefone, "telefoneReentrada");
		binder.bind(observacao, "observacao");

		binder.setBean(this.statusProtocoloGson);
	}

	private void confirmarClickListener(Button.ClickEvent evt) {
		if(binder.isValid()) {
			this.protocolo.getStatusProtocolo().add(new StatusProtocolo(this.status, LocalDateTime.now(),  null,binder.getBean(), ((MyUI) UI.getCurrent()).getUsuarioLogado()));

			eventBus.publish(this, new ProtocoloEvent.Reentrada());
			//TODO deve salvar os dados refernte a esse status
			this.window.close();

		}else{
			Notification.show("Ops ;) Acho que faltou preencher algum campo corretamente", Notification.Type.ERROR_MESSAGE);
		}
	}
}
