package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.BuscarProtocolos;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.repository.StatusRepository;
import br.com.exmart.rtdpjlite.service.ClienteService;
import br.com.exmart.rtdpjlite.service.NaturezaService;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.service.filter.ProtocoloFiltro;
import br.com.exmart.util.BeanLocator;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.UI;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



public class BuscarProtocolosImpl extends BuscarProtocolos{

	ProtocoloService protocoloService;
	private ListDataProvider<Protocolo> dataProvider;
	TipoProtocolo tipos = null;
	ConfigurableFilterDataProvider<Protocolo, Void, ProtocoloFiltro> filteredProvider;

	// TROCAR TÍTULO //
	public BuscarProtocolosImpl() {
		this.protocoloService = BeanLocator.find(ProtocoloService.class);
		dtFinal.setValue(LocalDate.now());
		dtInicial.setValue(LocalDate.now());
		dtFinal.setRangeEnd(LocalDate.now().plusDays(1));
		dtInicial.setRangeEnd(LocalDate.now().plusDays(1));
//		dtInicial.setValue(LocalDate.of(1900,1,1));

		comboCliente.setItemCaptionGenerator(Cliente::getNome);
		comboCliente.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},BeanLocator.find(ClienteService.class).findAll());
//		btnFiltrarData.addClickListener(evt->mostrarFiltro());
		btnFiltros.addClickListener(evt -> mostrarFiltro());
		switchTDPJ.addSelectionListener(evt -> trocarEspecialidade());


		comboAndamento.setItems(((MyUI) UI.getCurrent()).getStatus());
		comboAndamento.setItemCaptionGenerator(Status::getNome);
		comboNatureza.setItems(((MyUI) UI.getCurrent()).getNaturezas());
		comboNatureza.setItemCaptionGenerator(Natureza::getNome);
		comboAndamento.setPageLength(300);
		comboNatureza.setPageLength(300);
		comboCliente.setPageLength(300);

		DataProvider<Protocolo, ProtocoloFiltro> myProvider =
				DataProvider.fromFilteringCallbacks(query -> {
					return  protocoloService.findAll(query.getOffset(),query.getLimit(), query.getFilter()).stream();}, query -> {
					return protocoloService.countFindAll(query.getFilter());});
		filteredProvider = myProvider.withConfigurableFilter();
		gridProtocolo.setDataProvider(filteredProvider);
		tipoProtocolo.setEmptySelectionAllowed(false);
		setarTipos("TODOS");

		comboNatureza.addValueChangeListener(evt->filtarProtocolo());
		comboAndamento.addValueChangeListener(evt->filtarProtocolo());
		dtInicial.addValueChangeListener(evt->filtarProtocolo());
		dtFinal.addValueChangeListener(evt->filtarProtocolo());
		comboData.addValueChangeListener(evt->filtarProtocolo());
		tipoProtocolo.addValueChangeListener(evt->tipoProtocoloListener(evt));
		busca.addValueChangeListener(evt -> filtarProtocolo());
		comboCliente.addValueChangeListener(evt->filtarProtocolo());


//		tipoProtocolo.addValueChangeListener(evt -> {setarTipos(evt.getValue());});
	}


	private void trocarEspecialidade() {
		panelSwitch.setStyleName("bg-pj");
		panelLine.setStyleName("bg-pj");
	}

	private void tipoProtocoloListener(HasValue.ValueChangeEvent<String> evt) {
		setarTipos(evt.getValue());
	}

	private void setarTipos(String tipo){
		if(tipo == null){
			tipo =  "TODOS OS PROTOCOLOS";
		}
		if(tipo.toUpperCase().equals("PROTOCOLOS DE CERTIDÃO - PJ")) {
			tipos = TipoProtocolo.CERTIDAO_PJ;
		}else if (tipo.toUpperCase().equals("PROTOCOLOS DE CERTIDÃO - TD")){
			tipos = TipoProtocolo.CERTIDAO_TD;
		}else if (tipo.toUpperCase().equals("PROTOCOLOS DE EXAME E CÁLCULO - TD")){
			tipos = TipoProtocolo.EXAMECALCULO_TD;
		}else if (tipo.toUpperCase().equals("PROTOCOLOS DE EXAME E CÁLCULO - PJ")){
				tipos = TipoProtocolo.EXAMECALCULO_PJ;
		}else if (tipo.toUpperCase().equals("PROTOCOLOS DE REGISTRO - PJ")){
			tipos = TipoProtocolo.PRENOTACAO_PJ;
		}else if (tipo.toUpperCase().equals("PROTOCOLOS DE REGISTRO - TD")){
			tipos = TipoProtocolo.PRENOTACAO_TD;
		}else {
			tipos= null;
		}
		filtarProtocolo();
	}

	private void mostrarFiltro() {
		panelFiltros.setVisible(!panelFiltros.isVisible());
		comboData.clear();
		comboAndamento.clear();
		comboNatureza.clear();
	}




	private void filtarProtocolo(){
		Long numero = null;
		String buscaTexto = null;
		try{
			numero = Long.parseLong(busca.getValue());
		}catch (Exception e ){
			buscaTexto = busca.getValue();
			System.out.println("busca por texto: " + buscaTexto);
		}
		ProtocoloFiltro filtro = new ProtocoloFiltro(dtInicial.getValue(),dtFinal.getValue(), numero, comboAndamento.getValue(), comboNatureza.getValue(), comboData.getValue(), this.tipos, comboCliente.getValue(), buscaTexto);
		filteredProvider.setFilter(filtro);
	}

}