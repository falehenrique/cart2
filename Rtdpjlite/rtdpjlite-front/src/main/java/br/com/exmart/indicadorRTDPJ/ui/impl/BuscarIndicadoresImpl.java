package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.BuscarIndicadores;
import br.com.exmart.indicadorRTDPJ.ui.component.GridIndicadoresPessoal;
import br.com.exmart.indicadorRTDPJ.ui.util.Especialidade;
import br.com.exmart.rtdpjlite.model.IndicadorPessoalVO;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.util.BeanLocator;
import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

import java.util.List;

@SpringView(name = "indicadores")
@UIScope
@MenuCaption("INDICADORES")
@MenuIcon(VaadinIcons.USER_CARD)
public class BuscarIndicadoresImpl extends BuscarIndicadores implements View{


	private ProtocoloService protocoloService;

	public BuscarIndicadoresImpl(){
		this.protocoloService = BeanLocator.find(ProtocoloService.class);
		tipoIndicador.addValueChangeListener(evt->tipoIndicadorListener(evt));

		btnBuscarIndicadorPessoal.addClickListener(evt->btnBuscarIndicadorPessoalListener());
		btnBuscarIndicadorReal.addClickListener(evt->btnBuscarIndicadorRealListener());
		tipoIndicador.setEmptySelectionAllowed(false);
		btnFiltrarData.addClickListener(evt->mostrarFiltro());
	}

	private void btnBuscarIndicadorRealListener() {

	}

	private void btnBuscarIndicadorPessoalListener() {
		GridIndicadoresPessoal grid  = new GridIndicadoresPessoal();
		List<IndicadorPessoalVO> retorno = protocoloService.findIndicadorPessoal(txtBuscaPessoal.getValue(), getTipobusca());
		grid.setItems(retorno);
		grid.addComponentColumn(servico -> {
			Button button = new Button(VaadinIcons.EYE);
			button.setDescription("Visualizar Registro");
			button.addStyleName("borderless");
			button.addClickListener(click -> {
//				Protocolo registro = protocoloService.findByNumeroRegistroAndTipo(servico.getNumeroRegistro(), TipoProtocolo.fromCode(servico.getTipoProtocolo().intValue()));
//				GerenciarJanela.abrirJanela("", 90, 90, new ViewRegistroImpl(registro));
			});
			return button;
		}).setWidth(110);
		grid.addComponentColumn(servico -> {
			Button button = new Button(VaadinIcons.FILE_ADD);
			button.setDescription("Protocolar Certidão");
			button.addStyleName("borderless");
			button.addClickListener(click -> {
			});
//					Notification.show("Clicked: " + servico.toString(), Notification.Type.TRAY_NOTIFICATION));
			return button;
		}).setWidth(110);


		resultado.removeAllComponents();
		resultado.addComponentsAndExpand(grid);
	}

	private void tipoIndicadorListener(HasValue.ValueChangeEvent<String> evt) {
		if(evt.getValue().startsWith("INDICADOR PESSOAL")){
			buscaReal.setVisible(false);
			buscaPessoal.setVisible(true);
		}else{
			buscaReal.setVisible(true);
			buscaPessoal.setVisible(false);
		}

	}

	private Integer getTipobusca() {
		if(tipoIndicador.getValue().contains("PJ")){
			return TipoProtocolo.PRENOTACAO_PJ.getId();
		}else{
			return TipoProtocolo.PRENOTACAO_TD.getId();
		}
	}

	private void mostrarFiltro() {
		filtroData.setVisible(!filtroData.isVisible());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		if(((MyUI) UI.getCurrent()).getEspecialidadeAtual().equals(Especialidade.PJ)){
			tipoIndicador.setValue("INDICADOR PESSOAL PJ");
		}else{
			tipoIndicador.setValue("INDICADOR PESSOAL TD");
		}

	}
}
