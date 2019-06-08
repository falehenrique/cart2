package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.TabMenu;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.indicadorRTDPJ.ui.util.Especialidade;
import br.com.exmart.indicadorRTDPJ.ui.view.BuscarPedidoView;
import br.com.exmart.indicadorRTDPJ.ui.view.BuscarProtocoloView;
import br.com.exmart.util.BeanLocator;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import org.vaadin.spring.events.EventBus;

public class TabMenuImpl extends TabMenu implements View{

    private EventBus.SessionEventBus eventBus;
    private final NavigationManager navigationManager;


    public TabMenuImpl() {
        this.navigationManager = BeanLocator.find(NavigationManager.class);
        this.eventBus = BeanLocator.find(EventBus.SessionEventBus.class);

        eventBus.subscribe(this);
        attachNavigation(btnBalcaoTD, BalcaoImpl.class, null);
        attachNavigation(btnBalcaoPJ, BalcaoImpl.class, null);

        attachNavigation(btnPrenotacaoTD, ViewPrenotacaoTd.class, null);
        attachNavigation(btnExameTD, ViewProtocoloExameCalculoTd.class, null);
        attachNavigation(btnCertidaoTD, ViewProtocoloCertidaoTd.class, null);

        attachNavigation(btnPrenotacaoPJ, ViewPrenotacaoPj.class, null);
        attachNavigation(btnExamePJ, ViewProtocoloExameCalculoPj.class, null);
        attachNavigation(btnCertidaoPJ, ViewProtocoloCertidaoPj.class, null);

        attachNavigation(btnBuscarPedidosTD, BuscarPedidoView.class, null);
        attachNavigation(btnBuscarProtocolosTD, BuscarProtocoloView.class, "prenotacao");
        attachNavigation(btnBuscarIndicadoresTD, BuscarIndicadoresImpl.class, null);
        attachNavigation(btnBuscarLivrosTD, BuscarIndicadoresImpl.class,null);

        attachNavigation(btnBuscarPedidosPJ, BuscarPedidoView.class, null);
        attachNavigation(btnBuscarProtocolosPJ, BuscarProtocoloView.class, "prenotacao");
        attachNavigation(btnBuscarIndicadoresPJ, BuscarIndicadoresImpl.class, null);
        attachNavigation(btnBuscarLivrosPJ, BuscarIndicadoresImpl.class,null);

        attachNavigation(btnConfigTD, ViewConfigImpl.class,null);
        attachNavigation(btnConfigPJ, ViewConfigImpl.class,null);

        attachNavigation(btnAdminTD, ViewAdminImpl.class,null);
        attachNavigation(btnAdminPJ, ViewAdminImpl.class,null);
        tabMenu.addSelectedTabChangeListener(evt -> mudarEspecialidade(evt));
    }
    private void attachNavigation(Button navigationButton, Class<? extends View> targetView, String args) {
//        if(args == null){
//            navigationButton.addClickListener(e -> navigationManager.navigateTo(targetView));
//        }else {
//            navigationButton.addClickListener(e -> navigationManager.navigateTo(targetView, args));
//        }
    }
    private void mudarEspecialidade(TabSheet.SelectedTabChangeEvent evt) {
        ((MyUI) UI.getCurrent()).setEspecialidadeAtual(Especialidade.valueOf(evt.getTabSheet().getSelectedTab().getId()));
    }
}
