package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.InformeSelos;
import br.com.exmart.rtdpjlite.service.rest.seloeletronico.SeloEletronicoRest;
import br.com.exmart.rtdpjlite.vo.seloeletronico.InformeSelosRetornoVO;
import br.com.exmart.rtdpjlite.vo.seloeletronico.InformeSelosVO;
import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.google.common.base.Strings;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.util.List;

@SpringView(name = "informe_selo")
@UIScope
@MenuCaption("Informe de Selo")
@MenuIcon(VaadinIcons.CHART)
public class InformeSelosImpl extends InformeSelos implements View {

    public InformeSelosImpl() {
        groupTipoListarSelo.addSelectionListener(evt->listarSelos(evt.getSelectedItem().get()));
        btnInformar.addClickListener(evt->enviarSelos());
        seloEnviarGrid.addItemClickListener(item-> listarErro(item.getItem()));
    }

    private void listarErro(InformeSelosVO item) {
        if(item.getErros().size() > 0){
            Notification.show("Lista de Erros", item.listarErros(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private void enviarSelos() {
        String erro ="";
        List<InformeSelosRetornoVO> retorno = seloEletronicoRest.enviarSelo();
        for(InformeSelosRetornoVO retornoVO : retorno){
            if(retornoVO.getTotalErros() > 0){
                erro = erro + " , "+ retornoVO.getSelo();
            }
        }
        if(!Strings.isNullOrEmpty(erro)){
            Notification.show("Atenção", "Foram encontrado erros no envio dos selos abaixo \n" + erro, Notification.Type.ERROR_MESSAGE);
        }else{
            Notification.show("Envio", "Envio finalzado com sucesso.", Notification.Type.WARNING_MESSAGE);
            listarSelos("envio");
        }
    }

    private void listarSelos(String tipo) {
        try {
            seloEnviarGrid.setItems(seloEletronicoRest.listarSelo(tipo));
        }catch (ResourceAccessException ce){
            Notification.show("Erro","Erro ao acessar o seriço de selos", Notification.Type.ERROR_MESSAGE);
        }
        seloEnviarGrid.getDataProvider().refreshAll();
    }

    @Autowired
    private SeloEletronicoRest seloEletronicoRest;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        listarSelos("envio");
    }
}
