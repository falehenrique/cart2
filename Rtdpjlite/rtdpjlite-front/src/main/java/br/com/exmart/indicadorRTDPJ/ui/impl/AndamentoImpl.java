package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.Andamento;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.StatusProtocolo;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;

public class AndamentoImpl extends Andamento{

	public AndamentoImpl(Protocolo protocolo) {
		gridAndamento.removeAllColumns();
		gridAndamento.addColumn(item -> {return item.getStatus().getNome();}).setCaption("Valor");
		gridAndamento.addColumn(StatusProtocolo::getData).setCaption("Dia").setRenderer(new LocalDateTimeRenderer()).setId("Dia");
		gridAndamento.addColumn(StatusProtocolo::getUsuario).setCaption("Usuário");
//		gridAndamento.addColumn(item -> {if(!Strings.isNullOrEmpty(item.getConteudo())) {
//			return item.getConteudo().contains("\"possuiAnexo\":true");
//		}else{
//			return false;
//		}}).setCaption("Arquivo");
		gridAndamento.setItems(protocolo.getStatusProtocolo());

		gridAndamento.sort("Dia", SortDirection.ASCENDING);
	}

}
