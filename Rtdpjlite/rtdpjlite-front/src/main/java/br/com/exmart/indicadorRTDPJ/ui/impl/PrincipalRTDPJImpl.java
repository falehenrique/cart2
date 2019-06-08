package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.MainView;
import br.com.exmart.indicadorRTDPJ.ui.PrincipalRTDPJ;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.EspecialidadeEvent;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.indicadorRTDPJ.ui.util.Especialidade;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderTabPosition;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import javax.annotation.PostConstruct;

@UIScope
@SpringView
public class PrincipalRTDPJImpl extends PrincipalRTDPJ implements View{

	@Autowired//https://github.com/peholmst/vaadin4spring/blob/master/addons/eventbus/README.md
	private EventBus.SessionEventBus eventBus;

	@EventBusListenerMethod(scope = EventScope.SESSION)
	public void removerItemPedidoResumo(EspecialidadeEvent evento){
		if(evento.getEspecialidade().compareTo(Especialidade.PJ) == 0){
			tabMenu.setSelectedTab(1);
		}else{
			tabMenu.setSelectedTab(0);
		}
	}
	private final NavigationManager navigationManager;



	MainView mainView;

	@PostConstruct
	public void init() {
		eventBus.subscribe(this);
//		attachNavigation(btnBalcaoTD, BalcaoImpl.class, null);
//		attachNavigation(btnBalcaoPJ, BalcaoImpl.class, null);
//
//		attachNavigation(btnPrenotacaoTD, ViewPrenotacaoTd.class, null);
//		attachNavigation(btnExameTD, ViewProtocoloExameCalculoTd.class, null);
//		attachNavigation(btnCertidaoTD, ViewProtocoloCertidaoTd.class, null);
//
//		attachNavigation(btnPrenotacaoPJ, ViewPrenotacaoPj.class, null);
//		attachNavigation(btnExamePJ, ViewProtocoloExameCalculoPj.class, null);
//		attachNavigation(btnCertidaoPJ, ViewProtocoloCertidaoPj.class, null);
//
//		attachNavigation(btnBuscarPedidosTD, BuscarPedidosImpl.class, null);
//		attachNavigation(btnBuscarProtocolosTD, BuscarProtocoloView.class, "prenotacao");
//		attachNavigation(btnBuscarIndicadoresTD, BuscarIndicadoresImpl.class, null);
//		attachNavigation(btnBuscarLivrosTD, BuscarIndicadoresImpl.class,null);
//
//		attachNavigation(btnBuscarPedidosPJ, BuscarPedidosImpl.class, null);
//		attachNavigation(btnBuscarProtocolosPJ, BuscarProtocoloView.class, "prenotacao");
//		attachNavigation(btnBuscarIndicadoresPJ, BuscarIndicadoresImpl.class, null);
//		attachNavigation(btnBuscarLivrosPJ, BuscarIndicadoresImpl.class,null);
//
//		attachNavigation(btnConfigTD, ViewConfigImpl.class,null);
//		attachNavigation(btnConfigPJ, ViewConfigImpl.class,null);
//
//		attachNavigation(btnAdminTD, ViewAdminImpl.class,null);
//		attachNavigation(btnAdminPJ, ViewAdminImpl.class,null);

		SliderPanel sliderMenu = new SliderPanelBuilder(conteudoMenu())
				.expanded(false)
				.mode(SliderMode.LEFT)
				.caption("MENU")
				.tabPosition(SliderTabPosition.BEGINNING)
				.build();

		addComponent(sliderMenu);
	}
	private Component conteudoMenu() {
		Component inicio = new TabMenuImpl();
		return inicio;
	}

//	private void attachNavigation(Button navigationButton, Class<? extends View> targetView, String args) {
//		if(args == null){
//			navigationButton.addClickListener(e -> navigationManager.navigateTo(targetView));
//		}else {
//			navigationButton.addClickListener(e -> navigationManager.navigateTo(targetView, args));
//		}
//	}

	@Autowired
	public PrincipalRTDPJImpl(NavigationManager navigationManager, MainView mainView) {
		this.navigationManager = navigationManager;
		this.mainView = mainView;
		this.mainView.setWidth("100%");
		this.mainView.setHeight("100%");
		content.addComponentsAndExpand(mainView);
//		((MyUI) UI.getCurrent()).configurarNavegador(content);


//		content.addComponent(new BalcaoImpl(this));
//		btnRecepcao.addClickListener(evt -> {content.removeAllComponents(); content.addComponent(new BuscarPedidosImpl(100, new ViewProtocoloCertidaoImpl()));});		
		
//		//MENU INÍCIO TD//
//		btnDashboardTD.addClickListener(evt -> {content.removeAllComponents(); content.addComponent(new DashboardImpl());});
//
//		//MENU INÍCIO PJ//
//		btnDashboardPJ.addClickListener(evt -> {content.removeAllComponents(); content.addComponent(new DashboardImpl());});
		
		
		//MENU NOVO TD//

		//MENU NOVO PJ//

		
		//MENU PROTOCOLOS TD//


		//MENU PROTOCOLOS PJ//

		
		//MENU INDICADORES TD//
//		btnIndRealTD.addClickListener(evt -> {content.removeAllComponents(); content.addComponent(new BuscarIndicadoresImpl("INDICADOR REAL - RTD"));});
//		btnIndPessoalTD.addClickListener(evt -> {content.removeAllComponents(); content.addComponent(new BuscarIndicadoresImpl("INDICADOR PESSOAL - RTD"));});
//		//MENU INDICADORES PJ//
//		btnIndPessoalPJ.addClickListener(evt -> {content.removeAllComponents(); content.addComponent(new BuscarIndicadoresImpl("INDICADOR PESSOAL - PJ"));});
		
		//MENU LIVROS TD//
//		btnLvATD.addClickListener(evt -> {content.removeAllComponents(); content.addComponent(new BuscarIndicadoresImpl("LIVRO A - RTD"));});
//		btnLvBTD.addClickListener(evt -> {content.removeAllComponents(); content.addComponent(new BuscarIndicadoresImpl("LIVRO B - RTD"));});
//
//		//MENU LIVROS PJ//
//		btnLvAPJ.addClickListener(evt -> {content.removeAllComponents(); content.addComponent(new BuscarIndicadoresImpl("LIVRO A - PJ"));});
//		btnLvBPJ.addClickListener(evt -> {content.removeAllComponents(); content.addComponent(new BuscarIndicadoresImpl("LIVRO B - PJ"));});


		tabMenu.addSelectedTabChangeListener(evt -> mudarEspecialidade(evt));
//	
	}

	private void mudarEspecialidade(TabSheet.SelectedTabChangeEvent evt) {
		((MyUI) UI.getCurrent()).setEspecialidadeAtual(Especialidade.valueOf(evt.getTabSheet().getSelectedTab().getId()));
	}


//	public void mudarCores() {
//		menuGde.setStyleName("bg-blue-grey600");
//	}



	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	public ComponentContainer getNavigatorContent() {
		// TODO Auto-generated method stub
		return null;
	}

}
