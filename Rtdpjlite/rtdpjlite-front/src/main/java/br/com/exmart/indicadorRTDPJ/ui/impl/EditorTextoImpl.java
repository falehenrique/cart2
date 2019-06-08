package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.EditorTexto;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.rtdpjlite.model.ChecklistItem;
import br.com.exmart.rtdpjlite.model.Modelo;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.util.DocumentoUtil;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import br.com.exmart.util.BeanLocator;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.vaadin.spring.events.EventBus;

import java.util.List;

public class EditorTextoImpl extends EditorTexto{

	private final Protocolo protocolo;
	private final JtwigModel jtwigModel;
	private List<ChecklistItem> itensNaoChecados;
	private Window window;
	private EventBus.ViewEventBus eventBus;
	private ACAO acao;


	public EditorTextoImpl(List<ChecklistItem> itensNaoChecados, List<Modelo> etiquetas, Protocolo protocolo, ACAO acao, EventBus.ViewEventBus eventBus) {
		this.eventBus = eventBus;
		this.protocolo = protocolo;
		this.itensNaoChecados = itensNaoChecados;
		this.acao = acao;


		comboModelos.setItems(etiquetas);
		comboModelos.setItemCaptionGenerator(Modelo::getNome);
		comboModelos.addValueChangeListener(valueChangeEvent -> aplicarModelo(valueChangeEvent));


		jtwigModel  = BeanLocator.find(DocumentoUtil.class).iniciarVariaveisModelo(null, this.protocolo, this.itensNaoChecados, ((MyUI) UI.getCurrent()).getCustasCartorio(), null, this.protocolo.getPartesProtocolo(), this.protocolo.getObjetos(), protocolo.getIntimacaosProtocolo(), null);



		btnSalvar.addClickListener(evt-> salvarClickListener(evt));
		btnCancelar.addClickListener(evt->cancelar(evt));
		for(Modelo etiqueta : etiquetas){
			comboModelos.setValue(etiqueta);
			break;

		}


	}

	private void cancelar(Button.ClickEvent evt) {
		this.window.close();
	}

	private void salvarClickListener(Button.ClickEvent evt) {
		StatusProtocoloJson json = new StatusProtocoloJson();
		json.setTexto(editor.getValue());
		if(this.acao.equals(ACAO.DEVOLVER_PROTOCOLO)) {
			this.eventBus.publish(this, new ProtocoloEvent.DevolverProtocolo(json));
		}else if(this.acao.equals(ACAO.ALTERAR_CARIMBO)){
			this.eventBus.publish(this, new ProtocoloEvent.CarimboAtualizado(editor.getValue()));
		}else if(this.acao.equals(ACAO.APTO_REGISTRO)){
			this.eventBus.publish(this, new ProtocoloEvent.AptoRegistroProtocolo(this.protocolo, json));
		}
		this.window.close();
	}

	public String getTextoEtiqueta(){
		return editor.getValue();
	}



	private void aplicarModelo(HasValue.ValueChangeEvent<Modelo> valueChangeEvent) {
		String modelo = valueChangeEvent.getValue().getModelo();

		JtwigTemplate jtwigTemplate =JtwigTemplate.inlineTemplate(modelo);

		editor.setValue(jtwigTemplate.render(jtwigModel));


		editor.focus();
		verificaChecklist();
	}

	private String verificaChecklist() {

		if(this.itensNaoChecados != null) {
			StringBuffer retorno = new StringBuffer("<ol>");
			for(ChecklistItem itemChecklist : this.itensNaoChecados) {
				retorno.append("<li>"+ itemChecklist.getTextoDevolucao()+ "</li>") ;
			}
			retorno.append("</ol>");
			return retorno.toString();
		}
		return "";
	}


	public void focus(){
//		comboModelos.setValue(modeloInicial);
		comboModelos.focus();
		comboModelos.setEmptySelectionAllowed(false);
	}

	public enum ACAO {
		ALTERAR_CARIMBO,
		DEVOLVER_PROTOCOLO,
		NOTIFICACAO,
		APTO_REGISTRO
	}
}
