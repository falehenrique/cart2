package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.Objetos;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.rtdpjlite.model.Objeto;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.service.ObjetoService;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloVO;
import br.com.exmart.util.BeanLocator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.ArrayList;
import java.util.List;

public class ObjetosImpl extends Objetos implements PassoProtocolo{

	protected static Logger logger= LoggerFactory.getLogger(ObjetosImpl.class);
	private ObjetoService objetoService;
	private EventBus.ViewEventBus eventBus;
	private Protocolo protocolo;
	public ObjetosImpl(Protocolo protocolo, ObjetoService objetoService, EventBus.ViewEventBus eventBus) {
		logger.debug("iniciou ObjetosImpl");
		this.protocolo = protocolo;
		this.eventBus = eventBus;
		this.objetoService = objetoService;
		List<Objeto> objetosList = ((MyUI) UI.getCurrent()).getObjetos();
		objetos.setItems(objetosList);
		btnAdicionarObjeto.addClickListener(evt -> adicionarObjetoClickListener(evt));
		this.eventBus.subscribe(this);
		if(this.protocolo.getObjetos() != null){
			for(ObjetoProtocoloVO objeto : this.protocolo.getObjetos()){
				Objeto selecionado = null;
				for(Objeto obj : objetosList){
					if(obj.getNome().equalsIgnoreCase( objeto.getNome())){
						selecionado = obj;
					}
				}
				panelObjetosList.addComponent(new CardObjetoImpl(this.protocolo.getPartesProtocolo(), selecionado, objeto, this.eventBus));
			}
		}
		logger.debug("acabou ObjetosImpl");
	}

	private void adicionarObjetoClickListener(Button.ClickEvent evt) {
		if(objetos.getValue() != null) {
			panelObjetosList.addComponent(new CardObjetoImpl(this.protocolo.getPartesProtocolo(),objetos.getValue(), null, this.eventBus),0);
		}else{
			Notification.show("Favor selecionar um objeto", Notification.Type.ERROR_MESSAGE);
		}
	}

	boolean habilitarObjeto = false;
	@Override
	public void habilitarForm(boolean habilitar, boolean icProtocoloEditavel) {
		if(!icProtocoloEditavel){
			habilitar = icProtocoloEditavel;
		}
		this.habilitarObjeto = habilitar;
		panelObjetosList.forEach(item->{((CardObjetoImpl) item).habilitaDesabilita(habilitarObjeto, icProtocoloEditavel);});
		panelObjetos.setVisible(habilitar);
	}

	@Override
	public void cancelar() {

	}

	@Override
	public void focus() {
		objetos.focus();
	}


	@Override
	public void validate() throws Exception {
		List<ObjetoProtocoloVO> objetosProtocolo = new ArrayList<>();
		panelObjetosList.forEach(item->{
			ObjetoProtocoloVO objeto = ((CardObjetoImpl) item).getObjeto();
			if(objeto.getAtributos().size() > 0) {
				objetosProtocolo.add(objeto);
			}
		});
		this.protocolo.setObjetos(objetosProtocolo);
	}




	@Override
	public void removerListener() {

	}

	@Override
	public void addListener() {

	}

	@EventBusListenerMethod(scope = EventScope.VIEW)
	public void objetoRemovido(ProtocoloEvent.ObjetoRemovido evento){
		panelObjetosList.removeComponent(evento.getObjeto());
	}
	@EventBusListenerMethod(scope = EventScope.VIEW)
	public void objetoAdicionado(ProtocoloEvent.ObjetoAdicionado evento){
		try {
			this.validate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
